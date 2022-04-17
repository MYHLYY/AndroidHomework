package com.bjsxt.entity;


import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

public class VertificationCode implements Serializable {

  private static final long serialVersionUID = -5784824095887368308L;
  /**
   * java date 转mysql dateTime
   * <p>
   * new java.sql.Timestamp(new Date().getTime())；
   * <p>
   * mysql dateTime转java date
   * <p>
   * new java.util.Date(new Timestamp().getTime)；
   */

  private long id;
  private String telephone;
  private String vertification_code;
  private Date create_time;


  public VertificationCode(long id, String telephone, String vertification_code, Date create_time) {
    this.id = id;
    this.telephone = telephone;
    this.vertification_code = vertification_code;
    this.create_time = create_time;
  }

  public VertificationCode(String telephone, String vertification_code, Date create_time) {
    this.telephone = telephone;
    this.vertification_code = vertification_code;
    this.create_time = create_time;
  }

  public VertificationCode(String telephone) {
    this.telephone = telephone;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }


  public String getVertification_code() {
    return vertification_code;
  }

  public void setVertification_code(String vertification_code) {
    this.vertification_code = vertification_code;
  }

  public Date getCreate_time() {
    return create_time;
  }

  public void setCreate_time(Date create_time) {
    this.create_time = create_time;
  }

  @Override
  public String toString() {
    return "Code{" +
            "id=" + id +
            ", telephone='" + telephone + '\'' +
            ", vertification_code='" + vertification_code + '\'' +
            ", create_time=" + create_time +
            '}';
  }
}
