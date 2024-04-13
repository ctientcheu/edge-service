package com.polarbookshop.edgeservice;

import java.util.List;

/**
 * @author clement.tientcheu@cerebrau.com
 * @project edge-service
 * @org Cerebrau
 */
public record User(
   String username,
   String firstName,
   String lastName,
   List<String> roles
) {}
