package com.aiyuns.tinkerplay.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Date;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author: aiYunS @Date: 2023年3月16日, 0016 下午 12:52:12 @Description: 搜索中的人员信息
 */
@Schema(name = "Es用户信息实体类")
@Document(indexName = "user")
public class EsUser implements Serializable {

  @Id
  @Schema(description = "用户ID")
  private Long id;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "姓氏")
  private String firstName;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "姓名")
  private String name;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "名字")
  private String lastName;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "地址")
  private String address;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "性别")
  private String sex;

  @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "出生日期")
  // @Field(analyzer = "ik_max_word", type = FieldType.Date)
  private Date birthday;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "证件号码")
  private String CARD;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "手机号码")
  private String PHONE;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "电子邮件")
  private String EMAIL;

  @Field(analyzer = "ik_max_word", type = FieldType.Text)
  @Schema(description = "关键字")
  private String keywords;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public Date getBirthday() {
    return birthday;
  }

  public void setBirthday(Date birthday) {
    this.birthday = birthday;
  }

  public String getCARD() {
    return CARD;
  }

  public void setCARD(String CARD) {
    this.CARD = CARD;
  }

  public String getPHONE() {
    return PHONE;
  }

  public void setPHONE(String PHONE) {
    this.PHONE = PHONE;
  }

  public String getEMAIL() {
    return EMAIL;
  }

  public void setEMAIL(String EMAIL) {
    this.EMAIL = EMAIL;
  }

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  @Override
  public String toString() {
    return "EsUser{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", address='"
        + address
        + '\''
        + ", sex='"
        + sex
        + '\''
        + ", birthday="
        + birthday
        + ", CARD='"
        + CARD
        + '\''
        + ", PHONE='"
        + PHONE
        + '\''
        + ", EMAIL='"
        + EMAIL
        + '\''
        + ", keywords='"
        + keywords
        + '\''
        + '}';
  }
}
