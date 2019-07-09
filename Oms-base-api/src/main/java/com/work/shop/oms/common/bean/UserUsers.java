package com.work.shop.oms.common.bean;

import java.util.Date;


public class UserUsers implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String userId;
	private Integer id;
	private String email;
	private String userName;
	private String cardNo;
	private String password;
	private String question;
	private String answer;
	private Short sex;
	private Date birthday;
	private Double userMoney;
	private Double frozenMoney;
	private Integer payPoints;
	private Integer rankPoints;
	private Integer addressId;
	private Date regTime;
	private Integer lastLogin;
	private Date lastTime;
	private String lastIp;
	private Integer visitCount;
	private Short isSpecialRank;
	private String salt;
	private String parentId;
	private Short flag;
	private String alias;
	private String msn;
	private String qq;
	private String officePhone;
	private String homePhone;
	private String mobilePhone;
	private Short isValidated;
	private Double creditLine;
	private Short isUnpopular;
	private Short isAutoRank;
	private Short isUpdate;
	private String promotionId;
	private Integer userType;
	private String mobile;
	private Integer isUnion;
	private String comeFrom;
	private String sourceCode;
	private Integer levelId;
	private Date levelExpire;

	// Constructors

	/** default constructor */
	public UserUsers() {
	}

	/** minimal constructor */
	public UserUsers(String userId, String userName, String password,
			Double userMoney, Double frozenMoney, Integer payPoints,
			Integer rankPoints, Short isUnpopular, Integer userType,
			Integer levelId) {
		this.userId = userId;
		this.userName = userName;
		this.password = password;
		this.userMoney = userMoney;
		this.frozenMoney = frozenMoney;
		this.payPoints = payPoints;
		this.rankPoints = rankPoints;
		this.isUnpopular = isUnpopular;
		this.userType = userType;
		this.levelId = levelId;
	}

	/** full constructor */
	public UserUsers(String userId, Integer id, String email, String userName,
			String cardNo, String password, String question, String answer,
			Short sex, Date birthday, Double userMoney, Double frozenMoney,
			Integer payPoints, Integer rankPoints, Integer addressId,
			Date regTime, Integer lastLogin, Date lastTime, String lastIp,
			Integer visitCount, Short isSpecialRank, String salt,
			String parentId, Short flag, String alias, String msn, String qq,
			String officePhone, String homePhone, String mobilePhone,
			Short isValidated, Double creditLine, Short isUnpopular,
			Short isAutoRank, Short isUpdate, String promotionId,
			Integer userType, String mobile, Integer isUnion, String comeFrom,
			String sourceCode, Integer levelId, Date levelExpire) {
		this.userId = userId;
		this.id = id;
		this.email = email;
		this.userName = userName;
		this.cardNo = cardNo;
		this.password = password;
		this.question = question;
		this.answer = answer;
		this.sex = sex;
		this.birthday = birthday;
		this.userMoney = userMoney;
		this.frozenMoney = frozenMoney;
		this.payPoints = payPoints;
		this.rankPoints = rankPoints;
		this.addressId = addressId;
		this.regTime = regTime;
		this.lastLogin = lastLogin;
		this.lastTime = lastTime;
		this.lastIp = lastIp;
		this.visitCount = visitCount;
		this.isSpecialRank = isSpecialRank;
		this.salt = salt;
		this.parentId = parentId;
		this.flag = flag;
		this.alias = alias;
		this.msn = msn;
		this.qq = qq;
		this.officePhone = officePhone;
		this.homePhone = homePhone;
		this.mobilePhone = mobilePhone;
		this.isValidated = isValidated;
		this.creditLine = creditLine;
		this.isUnpopular = isUnpopular;
		this.isAutoRank = isAutoRank;
		this.isUpdate = isUpdate;
		this.promotionId = promotionId;
		this.userType = userType;
		this.mobile = mobile;
		this.isUnion = isUnion;
		this.comeFrom = comeFrom;
		this.sourceCode = sourceCode;
		this.levelId = levelId;
		this.levelExpire = levelExpire;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCardNo() {
		return this.cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer() {
		return this.answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Short getSex() {
		return this.sex;
	}

	public void setSex(Short sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Double getUserMoney() {
		return this.userMoney;
	}

	public void setUserMoney(Double userMoney) {
		this.userMoney = userMoney;
	}

	public Double getFrozenMoney() {
		return this.frozenMoney;
	}

	public void setFrozenMoney(Double frozenMoney) {
		this.frozenMoney = frozenMoney;
	}

	public Integer getPayPoints() {
		return this.payPoints;
	}

	public void setPayPoints(Integer payPoints) {
		this.payPoints = payPoints;
	}

	public Integer getRankPoints() {
		return this.rankPoints;
	}

	public void setRankPoints(Integer rankPoints) {
		this.rankPoints = rankPoints;
	}

	public Integer getAddressId() {
		return this.addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public Date getRegTime() {
		return this.regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Integer getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Integer lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getLastTime() {
		return this.lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	public String getLastIp() {
		return this.lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	public Integer getVisitCount() {
		return this.visitCount;
	}

	public void setVisitCount(Integer visitCount) {
		this.visitCount = visitCount;
	}

	public Short getIsSpecialRank() {
		return this.isSpecialRank;
	}

	public void setIsSpecialRank(Short isSpecialRank) {
		this.isSpecialRank = isSpecialRank;
	}

	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getParentId() {
		return this.parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Short getFlag() {
		return this.flag;
	}

	public void setFlag(Short flag) {
		this.flag = flag;
	}

	public String getAlias() {
		return this.alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getMsn() {
		return this.msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getQq() {
		return this.qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getOfficePhone() {
		return this.officePhone;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getHomePhone() {
		return this.homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public Short getIsValidated() {
		return this.isValidated;
	}

	public void setIsValidated(Short isValidated) {
		this.isValidated = isValidated;
	}

	public Double getCreditLine() {
		return this.creditLine;
	}

	public void setCreditLine(Double creditLine) {
		this.creditLine = creditLine;
	}

	public Short getIsUnpopular() {
		return this.isUnpopular;
	}

	public void setIsUnpopular(Short isUnpopular) {
		this.isUnpopular = isUnpopular;
	}

	public Short getIsAutoRank() {
		return this.isAutoRank;
	}

	public void setIsAutoRank(Short isAutoRank) {
		this.isAutoRank = isAutoRank;
	}

	public Short getIsUpdate() {
		return this.isUpdate;
	}

	public void setIsUpdate(Short isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getPromotionId() {
		return this.promotionId;
	}

	public void setPromotionId(String promotionId) {
		this.promotionId = promotionId;
	}

	public Integer getUserType() {
		return this.userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getIsUnion() {
		return this.isUnion;
	}

	public void setIsUnion(Integer isUnion) {
		this.isUnion = isUnion;
	}

	public String getComeFrom() {
		return this.comeFrom;
	}

	public void setComeFrom(String comeFrom) {
		this.comeFrom = comeFrom;
	}

	public String getSourceCode() {
		return this.sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public Integer getLevelId() {
		return this.levelId;
	}

	public void setLevelId(Integer levelId) {
		this.levelId = levelId;
	}

	public Date getLevelExpire() {
		return this.levelExpire;
	}

	public void setLevelExpire(Date levelExpire) {
		this.levelExpire = levelExpire;
	}

}