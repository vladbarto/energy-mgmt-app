package ro.tucn.energy_mgmt_backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Possible exceptions
 */
@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    ERR001_FACULTY_NOT_FOUND("Faculty with ID %s not found"),
    ERR002_USERNAME_NOT_FOUND("Username %s not found"),
    ERR099_INVALID_CREDENTIALS("Invalid credentials."),

    ERR001_ANNOUNCEMENT_NOT_FOUND("Announcement with ID %s not found"),

    ERR001_BARESUBJECT_NOT_FOUND("Bare subject with ID %s not found"),

    ERR001_UNIQUESUBJECT_NOT_FOUND("Unique subject with ID %s not found"),

    ERR001_PERSON_NOT_FOUND("Person with ID %s not found"),

    ERR001_REPRESENTATIVE_NOT_FOUND("Representative with ID %s not found"),

    ERR001_SECRETARY_NOT_FOUND("Secretary with ID %s not found"),

    ERR001_UNIVERSITY_NOT_FOUND("University with ID %s not found"),
    ERR002_UNIVERSITYCODE_NOT_FOUND("University %s not found");
    private final String message;
}
