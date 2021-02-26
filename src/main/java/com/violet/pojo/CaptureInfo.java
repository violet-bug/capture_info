package com.violet.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CaptureInfo {
    private String info;
    private String title;
    private String imaguri;
    private String price;
    private String company_address;
    private String company;
    private String pro_count;
    private String pro_spe;
    private String pro_info;
    private String pro_key;
}
