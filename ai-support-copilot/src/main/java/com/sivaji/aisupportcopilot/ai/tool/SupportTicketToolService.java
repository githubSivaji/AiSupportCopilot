package com.sivaji.aisupportcopilot.ai.tool;

import com.sivaji.aisupportcopilot.entity.SupportTicket;
import com.sivaji.aisupportcopilot.entity.User;
import com.sivaji.aisupportcopilot.repository.SupportTicketRepository;
import com.sivaji.aisupportcopilot.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.model.ToolContext;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupportTicketToolService {

    private final SupportTicketRepository supportTicketRepository;
    private final UserRepository userRepository;

    @Tool(
            description = "Create a support ticket for the currently authenticated user"
    )
    public String createSupportTicket(
            String issue,
            ToolContext toolContext) {

        UUID userId = (UUID) toolContext
                .getContext()
                .get("userId");

        User user = userRepository.findById(userId)
                .orElse(null);

        if (user == null) {
            return "Unable to create support ticket. User not found.";
        }

        SupportTicket ticket = SupportTicket.builder()
                .user(user)
                .description(issue)
                .build();

        SupportTicket saved =
                supportTicketRepository.save(ticket);

        return "Support ticket created successfully. Ticket ID: "
                + saved.getId();
    }
}
