package com.example.sen_scu.service.sen_csu;

import com.example.sen_scu.dto.sen_csu.PersonneChargeRequest;
import com.example.sen_scu.model.sen_csu.PersonneCharge;

public interface PersonneEnchargeSevice {
    PersonneCharge savePersonneChargeRequest(PersonneChargeRequest request, Long adherentId);
}
