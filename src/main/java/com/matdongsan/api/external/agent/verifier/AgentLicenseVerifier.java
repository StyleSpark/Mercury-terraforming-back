package com.matdongsan.api.external.agent.verifier;

import com.matdongsan.api.dto.agent.AgentRegisterRequest;

public interface AgentLicenseVerifier {
  boolean verify(AgentRegisterRequest request);
}
