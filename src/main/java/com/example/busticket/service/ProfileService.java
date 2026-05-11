package com.example.busticket.service;

import com.example.busticket.dto.ProfileUpdateRequest;
import com.example.busticket.entity.Ticket;
import com.example.busticket.entity.User;
import com.example.busticket.entity.UserProfile;
import com.example.busticket.repository.TicketRepository;
import com.example.busticket.repository.UserRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;

    public ProfileService(UserRepository userRepository, TicketRepository ticketRepository) {
        this.userRepository = userRepository;
        this.ticketRepository = ticketRepository;
    }

    @Transactional(readOnly = true)
    public User getProfile(Long userId) {
        return findUser(userId);
    }

    @Transactional(readOnly = true)
    public ProfileUpdateRequest getUpdateRequest(Long userId) {
        UserProfile profile = findUser(userId).getProfile();

        ProfileUpdateRequest request = new ProfileUpdateRequest();
        request.setFullName(profile.getFullName());
        request.setPhone(profile.getPhone());
        request.setEmail(profile.getEmail());
        request.setAddress(profile.getAddress());
        return request;
    }

    @Transactional
    public User updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = findUser(userId);
        UserProfile profile = user.getProfile();
        profile.setFullName(request.getFullName().trim());
        profile.setPhone(request.getPhone().trim());
        profile.setEmail(request.getEmail().trim());
        profile.setAddress(request.getAddress() == null ? "" : request.getAddress().trim());
        return user;
    }

    @Transactional(readOnly = true)
    public List<Ticket> getUserTickets(Long userId) {
        findUser(userId);
        return ticketRepository.findDetailsByUserId(userId);
    }

    private User findUser(Long userId) {
        return userRepository.findWithProfileById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy tài khoản"));
    }
}
