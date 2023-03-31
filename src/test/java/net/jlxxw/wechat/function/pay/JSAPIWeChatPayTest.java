package net.jlxxw.wechat.function.pay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.pay.jsapi.v3.AmountDTO;
import net.jlxxw.wechat.dto.pay.jsapi.v3.OrderInfoDTO;
import net.jlxxw.wechat.dto.pay.jsapi.v3.PayerDTO;
import net.jlxxw.wechat.dto.pay.jsapi.v3.SettleInfoDTO;
import net.jlxxw.wechat.properties.WeChatPayProperties;
import net.jlxxw.wechat.properties.WeChatProperties;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author zhanxiumei
 */
public class JSAPIWeChatPayTest extends BaseTest {

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private WeChatPayProperties weChatPayProperties;
    @Autowired
    private JSAPIWeChatPay JSAPIWeChatPay;

    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36";

    @Test
    public void createPrePayTest() {

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        AmountDTO amountDTO = new AmountDTO();
        amountDTO.setCurrency("CNY");
        amountDTO.setTotal(1);
        orderInfoDTO.setAmountDTO(amountDTO);
        orderInfoDTO.setAttach("");

        SettleInfoDTO settleInfoDTO = new SettleInfoDTO();
        settleInfoDTO.setProfitSharing(true);
        orderInfoDTO.setSettleInfoDTO(settleInfoDTO);
        orderInfoDTO.setDescription("测试数据");

        PayerDTO payerDTO = new PayerDTO();
        payerDTO.setSpOpenid(openId);
        payerDTO.setSubOpenid("");
        orderInfoDTO.setPayerDTO(payerDTO);
        orderInfoDTO.setGoodsTag("test_code");
        orderInfoDTO.setNotifyUrl("https://jlxxw.net");
        orderInfoDTO.setOutTradeNo(UUID.randomUUID().toString());
        orderInfoDTO.setSpAppId(weChatProperties.getAppId());
        orderInfoDTO.setSpMchId(weChatPayProperties.getMchId());
        orderInfoDTO.setSubAppId("");
        orderInfoDTO.setTimeExpire(LocalDate.now().plusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE));
        orderInfoDTO.setSubMchId("");
        JSAPIWeChatPay.createPrePay(orderInfoDTO, userAgent);

        Assert.assertFalse(ObjectUtils.isEmpty(orderInfoDTO));

    }


    @Test
    public void getExecutePayVOTest() throws Exception {
        String prePayId = "";
        JSAPIWeChatPay.getExecutePayVO(prePayId);
        Assert.assertFalse(StringUtils.isEmpty(prePayId));
    }
}
