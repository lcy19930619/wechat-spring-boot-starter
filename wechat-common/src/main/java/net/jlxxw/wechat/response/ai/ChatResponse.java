package net.jlxxw.wechat.response.ai;

import com.alibaba.fastjson.annotation.JSONField;
import net.jlxxw.wechat.response.WeChatResponse;

import java.util.List;

/**
 * ai bot 聊天结果
 * <br/>
 * <b>我也没太看懂这个文档的描述，我只知道 answer 应该就是结果。官方这文档写的，能挤出二斤水</b>
 * @see <a href="https://developers.weixin.qq.com/doc/aispeech/confapi/dialog/NLUresult.html">文档地址</a>
 * @author chunyang.leng
 * @date 2023-04-11 15:32
 */
public class ChatResponse extends WeChatResponse {

    /**
     * 技能id
     */
    @JSONField(name = "ans_node_id")
    private Integer ansNodeId;

    /**
     * 分类/技能名称
     */
    @JSONField(name = "ans_node_name")
    private String ansNodeName;

    /**
     * 命中的回答
     */
    @JSONField(name = "answer")
    private String answer;

    /**
     * 是否显示回复
     */
    @JSONField(name = "answer_open")
    private Integer answerOpen;

    /**
     * 回答的类型
     */
    @JSONField(name = "answer_type")
    private String answerType;

    /**
     *
     */
    @JSONField(name = "article")
    private String article;

    /**
     * 回答信心指数
     */
    @JSONField(name = "confidence")
    private Float confidence;

    /**
     *
     */
    @JSONField(name = "create_time")
    private String createTime;

    /**
     *
     */
    @JSONField(name = "dialog_session_status")
    private String dialogSessionStatus;

    /**
     *
     */
    @JSONField(name = "dialog_status")
    private String dialogStatus;

    /**
     *
     */
    @JSONField(name = "event")
    private String event;

    /**
     * 发起query的用户,对应签名接口的userid
     */
    @JSONField(name = "from_user_name")
    private String fromUserName;

    /**
     *
     */
    @JSONField(name = "intent_confirm_status")
    private String intentConfirmStatus;

    /**
     *
     */
    @JSONField(name = "is_default_answer")
    private Boolean isDefaultAnswer;

    /**
     *
     */
    @JSONField(name = "list_options")
    private Boolean listOptions;

    /**
     * 消息id
     */
    @JSONField(name = "msg_id")
    private String msgId;

    /**
     *
     */
    @JSONField(name = "opening")
    private String opening;

    /**
     *
     */
    @JSONField(name = "request_id")
    private Integer requestId;

    /**
     *
     */
    @JSONField(name = "ret")
    private Integer ret;

    /**
     *
     */
    @JSONField(name = "scene_status")
    private String sceneStatus;

    /**
     *
     */
    @JSONField(name = "session_id")
    private String sessionId;

    /**
     *
     */
    @JSONField(name = "skill_id")
    private String skillId;

    /**
     *
     */
    @JSONField(name = "skill_name")
    private String skillName;

    /**
     *
     */
    @JSONField(name = "slot_info")
    private List<?> slotInfo;

    /**
     *
     */
    @JSONField(name = "slots_info")
    private List<?> slotsInfo;

    /**
     * 机器人回复的状态, 可能的数值：FAQ、NOMATCH、CONTEXT_FAQ、GENERAL_FAQ、FAQ_RECOMMEND
     */
    @JSONField(name = "status")
    private String status;

    /**
     *
     */
    @JSONField(name = "take_options_only")
    private Boolean takeOptionsOnly;

    /**
     * 标准问题/意图名称
     */
    @JSONField(name = "title")
    private String title;

    /**
     * 接受query的机器人
     */
    @JSONField(name = "to_user_name")
    private String toUserName;

    /**
     *
     */
    @JSONField(name = "msgtype")
    private String msgtype;

    /**
     * 用户发送的消息
     */
    @JSONField(name = "query")
    private String query;

    /**
     *
     */
    @JSONField(name = "rid")
    private String rid;

    public Integer getAnsNodeId() {
        return ansNodeId;
    }

    public void setAnsNodeId(Integer ansNodeId) {
        this.ansNodeId = ansNodeId;
    }

    public String getAnsNodeName() {
        return ansNodeName;
    }

    public void setAnsNodeName(String ansNodeName) {
        this.ansNodeName = ansNodeName;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getAnswerOpen() {
        return answerOpen;
    }

    public void setAnswerOpen(Integer answerOpen) {
        this.answerOpen = answerOpen;
    }

    public String getAnswerType() {
        return answerType;
    }

    public void setAnswerType(String answerType) {
        this.answerType = answerType;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public Float getConfidence() {
        return confidence;
    }

    public void setConfidence(Float confidence) {
        this.confidence = confidence;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDialogSessionStatus() {
        return dialogSessionStatus;
    }

    public void setDialogSessionStatus(String dialogSessionStatus) {
        this.dialogSessionStatus = dialogSessionStatus;
    }

    public String getDialogStatus() {
        return dialogStatus;
    }

    public void setDialogStatus(String dialogStatus) {
        this.dialogStatus = dialogStatus;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getIntentConfirmStatus() {
        return intentConfirmStatus;
    }

    public void setIntentConfirmStatus(String intentConfirmStatus) {
        this.intentConfirmStatus = intentConfirmStatus;
    }

    public Boolean getIsDefaultAnswer() {
        return isDefaultAnswer;
    }

    public void setIsDefaultAnswer(Boolean isDefaultAnswer) {
        this.isDefaultAnswer = isDefaultAnswer;
    }

    public Boolean getListOptions() {
        return listOptions;
    }

    public void setListOptions(Boolean listOptions) {
        this.listOptions = listOptions;
    }


    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getOpening() {
        return opening;
    }

    public void setOpening(String opening) {
        this.opening = opening;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public String getSceneStatus() {
        return sceneStatus;
    }

    public void setSceneStatus(String sceneStatus) {
        this.sceneStatus = sceneStatus;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public List<?> getSlotInfo() {
        return slotInfo;
    }

    public void setSlotInfo(List<?> slotInfo) {
        this.slotInfo = slotInfo;
    }

    public List<?> getSlotsInfo() {
        return slotsInfo;
    }

    public void setSlotsInfo(List<?> slotsInfo) {
        this.slotsInfo = slotsInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getTakeOptionsOnly() {
        return takeOptionsOnly;
    }

    public void setTakeOptionsOnly(Boolean takeOptionsOnly) {
        this.takeOptionsOnly = takeOptionsOnly;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }
}
