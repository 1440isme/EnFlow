package vn.enflow.service;

import vn.enflow.dto.request.LoginRequest;
import vn.enflow.dto.request.RegisterRequest;
import vn.enflow.dto.respone.AuthResponse;

public interface IAuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
