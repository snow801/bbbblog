package com.wzn.ablog.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortRoleData {
    private String port_url;
    private String url_type;
    private String description;
    private String role_name;
}
