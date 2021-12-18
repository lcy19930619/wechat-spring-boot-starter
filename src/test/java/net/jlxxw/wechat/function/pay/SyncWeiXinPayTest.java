package net.jlxxw.wechat.function.pay;

import net.jlxxw.wechat.base.BaseTest;
import net.jlxxw.wechat.dto.pay.jsapi.v3.AmountDTO;
import net.jlxxw.wechat.dto.pay.jsapi.v3.OrderInfoDTO;
import net.jlxxw.wechat.dto.pay.jsapi.v3.PayerDTO;
import net.jlxxw.wechat.dto.pay.jsapi.v3.SettleInfoDTO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * @author zhanxiumei
 */
public class SyncWeiXinPayTest extends BaseTest {

    @Autowired
    private SyncWeiXinPay syncWeiXinPay;

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
        syncWeiXinPay.createPrePay(orderInfoDTO, userAgent);

        Assert.assertFalse(ObjectUtils.isEmpty(orderInfoDTO));

    }


    @Test
    public void getExecutePayVOTest() throws Exception {
        String prePayId="";
        syncWeiXinPay.getExecutePayVO(prePayId);
        Assert.assertFalse(StringUtils.isEmpty(prePayId));
    }
}
