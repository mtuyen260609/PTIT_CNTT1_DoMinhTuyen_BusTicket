package com.example.busticket.service;

import com.example.busticket.dto.BusRequest;
import com.example.busticket.entity.Bus;
import com.example.busticket.entity.BusTypeOption;
import com.example.busticket.repository.BusRepository;
import com.example.busticket.repository.TripRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final TripRepository tripRepository;

    public BusService(BusRepository busRepository, TripRepository tripRepository) {
        this.busRepository = busRepository;
        this.tripRepository = tripRepository;
    }

    @Transactional(readOnly = true)
    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    @Transactional(readOnly = true)
    public BusRequest findRequestById(Long id) {
        Bus bus = findBus(id);
        BusRequest request = new BusRequest();
        request.setLicensePlate(bus.getLicensePlate());
        request.setBusType(resolveBusType(bus));
        request.setCompany(bus.getCompany());
        request.setDriverName(bus.getDriverName());
        return request;
    }

    @Transactional
    public void create(BusRequest request) {
        String licensePlate = normalizePlate(request.getLicensePlate());
        if (busRepository.existsByLicensePlate(licensePlate)) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại");
        }

        Bus bus = new Bus();
        fillBus(bus, request, licensePlate);
        busRepository.save(bus);
    }

    @Transactional
    public void update(Long id, BusRequest request) {
        Bus bus = findBus(id);
        String licensePlate = normalizePlate(request.getLicensePlate());
        if (busRepository.existsByLicensePlateAndIdNot(licensePlate, id)) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại");
        }

        fillBus(bus, request, licensePlate);
    }

    @Transactional
    public void delete(Long id) {
        Bus bus = findBus(id);
        if (tripRepository.existsByBusId(id)) {
            throw new IllegalArgumentException("Không thể xóa xe đã được gán cho chuyến xe");
        }
        busRepository.delete(bus);
    }

    private Bus findBus(Long id) {
        return busRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy xe"));
    }

    private void fillBus(Bus bus, BusRequest request, String licensePlate) {
        BusTypeOption busType = request.getBusType();
        bus.setLicensePlate(licensePlate);
        bus.setBusType(busType.getLabel());
        bus.setTotalSeats(busType.getTotalSeats());
        bus.setCompany(normalize(request.getCompany()));
        bus.setDriverName(normalize(request.getDriverName()));
    }

    private BusTypeOption resolveBusType(Bus bus) {
        for (BusTypeOption option : BusTypeOption.values()) {
            if (option.getLabel().equals(bus.getBusType()) || option.getTotalSeats() == bus.getTotalSeats()) {
                return option;
            }
        }
        return BusTypeOption.SEAT_29;
    }

    private String normalizePlate(String value) {
        return value.trim().toUpperCase();
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
