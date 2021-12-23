package com.pplflw.empmgmt.representation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mkumar
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
public class ErrorRepresentation {
     private String timestamp;
     private String status;
     private String error;
     private String message;
     private String path;
}
