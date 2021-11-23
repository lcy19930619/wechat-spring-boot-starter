package net.jlxxw.component.weixin.function.pay;

import net.jlxxw.component.weixin.base.BaseTest;
import net.jlxxw.component.weixin.dto.pay.jsapi.v3.AmountDTO;
import net.jlxxw.component.weixin.dto.pay.jsapi.v3.OrderInfoDTO;
import net.jlxxw.component.weixin.dto.pay.jsapi.v3.PayerDTO;
import net.jlxxw.component.weixin.dto.pay.jsapi.v3.SettleInfoDTO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author zhanxiumei
 */
public class WeiXinPayTest extends BaseTest {

    @Autowired
    private WeiXinPay weiXinPay;

    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36";

    public void createPrePayTest() {

        OrderInfoDTO orderInfoDTO = new OrderInfoDTO();
        AmountDTO amountDTO = new AmountDTO();
        amountDTO.setCurrency("");
        amountDTO.setTotal(1);
        orderInfoDTO.setAmountDTO(amountDTO);
        orderInfoDTO.setAttach("");

        SettleInfoDTO settleInfoDTO = new SettleInfoDTO();
        settleInfoDTO.setProfitSharing(true);
        orderInfoDTO.setSettleInfoDTO(settleInfoDTO);
        orderInfoDTO.setDescription("");

        PayerDTO payerDTO = new PayerDTO();
        payerDTO.setSpOpenid("");
        payerDTO.setSubOpenid("");
        orderInfoDTO.setPayerDTO(payerDTO);
        orderInfoDTO.setGoodsTag("");
        orderInfoDTO.setNotifyUrl("");
        orderInfoDTO.setOutTradeNo("");
        orderInfoDTO.setSpAppId("");
        orderInfoDTO.setSpMchId("");
        orderInfoDTO.setSubAppId("");
        orderInfoDTO.setTimeExpire("");
        orderInfoDTO.setSubMchId("");
        weiXinPay.createPrePay(orderInfoDTO, userAgent);

        Assert.assertFalse(ObjectUtils.isEmpty(orderInfoDTO));

    }


    @Test
    public void getExecutePayVOTest() throws Exception {
        String prePayId="";
        weiXinPay.getExecutePayVO(prePayId);
        Assert.assertFalse(StringUtils.isEmpty(prePayId));
    }
}
