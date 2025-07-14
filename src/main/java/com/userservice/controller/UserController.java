package com.userservice.controller;

import com.userservice.dto.UserDTO;
import com.userservice.exception.EmailAlreadyExistsException;
import com.userservice.exception.NotFoundException;
import com.userservice.model.User;
import com.userservice.request.ListPageRequest;
import com.userservice.request.UserDeleteRequest;
import com.userservice.request.UserUpdateRequest;
import com.userservice.service.UserService;
import com.userservice.util.Response;
import com.userservice.util.Utils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.*;

/**
 * Controller class for handling user-related operations.
 * Provides REST endpoints for user management including CRUD operations.
 */
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The ID of the user to retrieve
     * @return Response containing the user data or error message
     */
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/")
    @Cacheable(value = "users", key = "#id")
    public Object findUserById(@Parameter(description = "User ID") @RequestParam String id) {
        User user = userService.findUserById(id);
        if (user == null) {
            log.warn("User not found with ID: {}", id);
            throw new NotFoundException("User Not Found");
        }
        log.info("User found with ID: {}", id);
        return Response.data(HttpStatus.OK.value(), "OK", "User Found", user);
    }

    /**
     * Adds a new user to the system.
     *
     * @param userDTO       The user data to add
     * @param bindingResult Validation result
     * @return Response containing the created user or error message
     */
    @Operation(summary = "Create new user", description = "Creates a new user in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/")
    public Object addUser(
            @Parameter(description = "User data to create") @Valid @RequestBody UserDTO userDTO,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in addUser: {}", bindingResult.getAllErrors());
            return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult), null);
        }

        UserDTO createdUser = userService.addUser(userDTO);
        if (createdUser == null) {
            log.warn("Failed to create user: {}", userDTO.getEmail());
            throw new EmailAlreadyExistsException("Email is already taken");
        }
        log.info("User created successfully: {}", createdUser.getEmail());
        return Response.data(HttpStatus.CREATED.value(), "CREATED", "User Added Successfully", createdUser);
    }

    /**
     * Retrieves a list of users with pagination.
     *
     * @param listPageRequest Pagination and filtering parameters
     * @param bindingResult   Validation result
     * @return Response containing the list of users or error message
     */
    @Operation(summary = "Get all users", description = "Retrieves a list of users with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "No users found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/list")
    public Object findAllUsers(
            @Parameter(description = "Pagination and filtering parameters") @Valid @RequestBody ListPageRequest listPageRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in findAllUsers: {}", bindingResult.getAllErrors());
            return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult), null);
        }

        List<User> users = userService.findAllUsers(listPageRequest);
        if (users == null || users.isEmpty()) {
            log.info("No users found with given criteria");
            return Response.data(HttpStatus.NOT_FOUND.value(), "NOT_FOUND", "No Records Found", null);
        }
        log.info("Found {} users", users.size());
        return Response.data(HttpStatus.OK.value(), "OK", "List Found", users);
    }

    /**
     * Updates an existing user.
     *
     * @param userUpdateRequest The user data to update
     * @param bindingResult     Validation result
     * @return Response containing the updated user or error message
     */
    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update")
    public Object updateUser(
            @Parameter(description = "User data to update") @Valid @RequestBody UserUpdateRequest userUpdateRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in updateUser: {}", bindingResult.getAllErrors());
            return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult), null);
        }

        User updatedUser = userService.updateUser(userUpdateRequest);
        if (updatedUser == null) {
            log.warn("User not found for update: {}", userUpdateRequest.getId());
            throw new NotFoundException("User not found");
        }
        log.info("User updated successfully: {}", updatedUser.getId());
        return Response.data(HttpStatus.OK.value(), "OK", "User Updated successfully", updatedUser);
    }

    /**
     * Deletes a user from the system.
     *
     * @param userDeleteRequest The user to delete
     * @param bindingResult     Validation result
     * @return Response containing the deletion result or error message
     */
    @Operation(summary = "Delete user", description = "Soft deletes a user from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/delete")
    public Object deleteUser(
            @Parameter(description = "User to delete") @Valid @RequestBody UserDeleteRequest userDeleteRequest,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in deleteUser: {}", bindingResult.getAllErrors());
            return Response.data(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST", Utils.getFieldError(bindingResult), null);
        }

        User deletedUser = userService.deleteUser(userDeleteRequest);
        if (deletedUser == null) {
            log.warn("User not found for deletion: {}", userDeleteRequest.getId());
            throw new NotFoundException("User not found");
        }
        log.info("User deleted successfully: {}", deletedUser.getId());
        return Response.data(HttpStatus.OK.value(), "OK", "User Deleted successfully", deletedUser.getId());
    }

    /**
     * Retrieves all courses associated with a user.
     *
     * @param userId The ID of the user
     * @return Response containing the list of courses or error message
     */
    @Operation(summary = "Get user courses", description = "Retrieves all courses associated with a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/all-courses-byuser")
    @Cacheable(value = "courses", key = "#userId")
    public Object findAllCoursesByUser(@Parameter(description = "User ID") @RequestParam String userId) {
        log.info("Finding courses for user: {}", userId);
        return userService.findAllCoursesByUserService(userId);
    }

    /**
     * Diagnostic endpoint to test HTTP request information.
     *
     * @param httpServletRequest  The HTTP request
     * @param httpServletResponse The HTTP response
     * @param ip                  Optional IP address parameter
     * @return Response containing HTTP request details
     */
    @Operation(summary = "Get HTTP request info", description = "Diagnostic endpoint to test HTTP request information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request information retrieved successfully")
    })
    @GetMapping("/guest/getHttp/test")
    public ResponseEntity<Map<String, Object>> getHttpREQ(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            @Parameter(description = "Optional IP address") @RequestParam(required = false) String ip) {

        log.info("HTTP diagnostic endpoint called");
        Map<String, Object> response = new LinkedHashMap<>();

        // Request information
        response.put("httpReqIp", httpServletRequest.getRemoteAddr());
        response.put("directReqIP", ip);
        response.put("X-Real-IP", httpServletRequest.getHeader("X-Real-IP"));
        response.put("User-Agent", httpServletRequest.getHeader("User-Agent"));
        response.put("Referer", httpServletRequest.getHeader("Referer"));
        response.put("host", httpServletRequest.getHeader("host"));
        response.put("x-forwarded-for", httpServletRequest.getHeader("x-forwarded-for"));
        response.put("accept", httpServletRequest.getHeader("accept"));
        response.put("sec-ch-ua-platform", httpServletRequest.getHeader("sec-ch-ua-platform"));

        // Client information
        Locale clientLocale = httpServletRequest.getLocale();
        Calendar calendar = Calendar.getInstance(clientLocale);
        TimeZone clientTimeZone = calendar.getTimeZone();
        response.put("clientTimeZone", clientTimeZone);
        response.put("clientDate", calendar.getTime());
        response.put("serverDate", new Date());

        // Memory information
        int byteToMb = (1024 * 1024);
        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryMXBean.getHeapMemoryUsage();
        response.put("heapMemoryUsage", heapMemoryUsage.getUsed() / byteToMb + " MB");
        response.put("maxHeapMemory", heapMemoryUsage.getMax() / byteToMb + " MB");

        return ResponseEntity.ok(response);
    }
}