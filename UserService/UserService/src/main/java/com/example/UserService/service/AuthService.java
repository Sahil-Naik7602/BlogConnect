package com.example.UserService.service;



import com.example.UserService.clients.ConnectionClient;
import com.example.UserService.dto.LoginRequestDto;
import com.example.UserService.dto.NodeCreationDTO;
import com.example.UserService.dto.SignupRequestDto;
import com.example.UserService.dto.UserDto;
import com.example.UserService.entity.User;
import com.example.UserService.exception.BadRequestException;
import com.example.UserService.exception.ResourceNotFoundException;
import com.example.UserService.repository.UserRepository;
import com.example.UserService.utils.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final ConnectionClient connectionClient;
        private final ModelMapper modelMapper;
        private final JwtService jwtService;
        private final Neo4jClient neo4jClient;


        @Transactional
        public UserDto signUp(SignupRequestDto signupRequestDto) {
                boolean exists = userRepository.existsByEmail(signupRequestDto.getEmail());
                if(exists) {
                        throw new BadRequestException("User already exists, cannot signup again.");
                }

                User user = modelMapper.map(signupRequestDto, User.class);
                user.setPassword(PasswordUtil.hashPassword(signupRequestDto.getPassword()));

                User savedUser = userRepository.save(user);

//                TODO: Handle this in connection Service Make an API there and call it through OpenFeign Client
                Long userId = savedUser.getId();
                String name = signupRequestDto.getName();
                connectionClient.createNode(new NodeCreationDTO(name,userId));
//                //Also create a node in the DB
//
//                String query = String.format("CREATE (p:Person:`%s` {userId: $userId, name: $name}) RETURN p", userId+"_"+name);
//
//                // Run the query safely with parameters
//                neo4jClient.query(query)
//                        .bind(userId).to("userId")
//                        .bind(name).to("name")
//                        .run();
                return modelMapper.map(savedUser, UserDto.class);
        }

        public String login(LoginRequestDto loginRequestDto) {
                User user = userRepository.findByEmail(loginRequestDto.getEmail())
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with email: "+loginRequestDto.getEmail()));

                boolean isPasswordMatch = PasswordUtil.checkPassword(loginRequestDto.getPassword(), user.getPassword());

                if(!isPasswordMatch) {
                        throw new BadRequestException("Incorrect password");
                }

                return jwtService.generateAccessToken(user);
        }


}
