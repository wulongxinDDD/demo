package cn.tedu.straw.sys.vo;


import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class FansVo implements Serializable {

    private String username;

    private String nickname;

    private String autograph;

    private String gender;

    private LocalDate birthday;

    private String img;

    private boolean isFriend = false;

    private boolean isMe = false;

}
