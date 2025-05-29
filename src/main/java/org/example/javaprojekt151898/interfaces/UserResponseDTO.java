package org.example.javaprojekt151898.interfaces;

import lombok.Data;
import org.example.javaprojekt151898.entity.UserRole;

@Data
public class UserResponseDTO {
    private Long id;
    private String username;
    private String loginEmail;
    private UserRole role;
// Jeśli chcesz „odchudzić” odpowiedź, nie musisz podawać tu candidate czy job offers.
// Możesz jednak dodać np. listę identyfikatorów utworzonych ofert, albo pewien minimalny zestaw informacji.
// private List<Long> jobOfferIds;
// private Long candidateId;
}