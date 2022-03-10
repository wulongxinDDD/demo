package cn.tedu.straw.portal;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

@SpringBootTest
class StrawPortalApplicationTests {

        public enum nodeStatus {
            DONTANSWERSHEET(0, "未接单", 0),
            HAVESENTSINGLE(1, "已派单", 0),
            HAVEORDER(2, "已接单", 0),
            DEFERREDAUDIT(3, "延期审核", 0),
            complete(4, "已完成", 0),
            CUSTOMSAUDIT(5, "非关审核", 0),
            ZONEAUDIT(6, "区域投诉审核", 0),
            TOTALAUDIT(7, "总部投诉审核", 0),
            RETURNVISIT(8, "回访", 0),
            TOBESIGN(9, "待签字", 0),
            ARCHIVE(10, "归档", 1),
            ABANDON(11, "废弃", 1),
            ALL(999, "全部", 999);

            private Integer tag;
            private String status;
            private Integer btnType;

            nodeStatus(Integer tag, String status, Integer btnType) {
                this.tag = tag;
                this.status = status;
                this.btnType = btnType;
            }

            public Integer getTag() {
                return tag;
            }

            public String getStatus() {
                return status;
            }

            public Integer getBtnType() {
                return btnType;
            }

            public static Integer getBtn(String status) {

                for (nodeStatus nodeStatus : nodeStatus.values()) {
                    if (Objects.equals(status, nodeStatus.getStatus())) {
                        return nodeStatus.getBtnType();
                    }
                }
                return null;
            }

        }
        @Test
        public void test(){
            System.out.println(StrawPortalApplicationTests.nodeStatus.values());
        }
    }

