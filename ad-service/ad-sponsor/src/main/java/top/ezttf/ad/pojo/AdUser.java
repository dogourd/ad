package top.ezttf.ad.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.ezttf.ad.constant.CommonStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_user")
public class AdUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "username", nullable = false)
    private String username;

    @Basic
    @Column(name = "token", nullable = false)
    private String token;

    @Basic
    @Column(name = "user_status", nullable = false)
    private Integer userStatus;

    @Basic
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public AdUser(String username, String token) {

        this.username = username;
        this.token = token;
        this.userStatus = CommonStatus.VALID.getStatus();
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }
}
