package com.work.shop.oms.bean;

import java.io.Serializable;
import java.util.Date;

public class QueueMqConfig implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8035296897697546723L;

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.server_ip
     *
     * @mbggenerated
     */
    private String serverIp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.queue_name
     *
     * @mbggenerated
     */
    private String queueName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.concurrency
     *
     * @mbggenerated
     */
    private String concurrency;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.queue_status
     *
     * @mbggenerated
     */
    private Byte queueStatus;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.note
     *
     * @mbggenerated
     */
    private String note;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.listeser_bean_name
     *
     * @mbggenerated
     */
    private String listeserBeanName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.add_time
     *
     * @mbggenerated
     */
    private Date addTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column queue_mq_config.update_time
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.id
     *
     * @return the value of queue_mq_config.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.id
     *
     * @param id the value for queue_mq_config.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.server_ip
     *
     * @return the value of queue_mq_config.server_ip
     *
     * @mbggenerated
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.server_ip
     *
     * @param serverIp the value for queue_mq_config.server_ip
     *
     * @mbggenerated
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.queue_name
     *
     * @return the value of queue_mq_config.queue_name
     *
     * @mbggenerated
     */
    public String getQueueName() {
        return queueName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.queue_name
     *
     * @param queueName the value for queue_mq_config.queue_name
     *
     * @mbggenerated
     */
    public void setQueueName(String queueName) {
        this.queueName = queueName == null ? null : queueName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.concurrency
     *
     * @return the value of queue_mq_config.concurrency
     *
     * @mbggenerated
     */
    public String getConcurrency() {
        return concurrency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.concurrency
     *
     * @param concurrency the value for queue_mq_config.concurrency
     *
     * @mbggenerated
     */
    public void setConcurrency(String concurrency) {
        this.concurrency = concurrency == null ? null : concurrency.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.queue_status
     *
     * @return the value of queue_mq_config.queue_status
     *
     * @mbggenerated
     */
    public Byte getQueueStatus() {
        return queueStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.queue_status
     *
     * @param queueStatus the value for queue_mq_config.queue_status
     *
     * @mbggenerated
     */
    public void setQueueStatus(Byte queueStatus) {
        this.queueStatus = queueStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.note
     *
     * @return the value of queue_mq_config.note
     *
     * @mbggenerated
     */
    public String getNote() {
        return note;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.note
     *
     * @param note the value for queue_mq_config.note
     *
     * @mbggenerated
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.listeser_bean_name
     *
     * @return the value of queue_mq_config.listeser_bean_name
     *
     * @mbggenerated
     */
    public String getListeserBeanName() {
        return listeserBeanName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.listeser_bean_name
     *
     * @param listeserBeanName the value for queue_mq_config.listeser_bean_name
     *
     * @mbggenerated
     */
    public void setListeserBeanName(String listeserBeanName) {
        this.listeserBeanName = listeserBeanName == null ? null : listeserBeanName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.add_time
     *
     * @return the value of queue_mq_config.add_time
     *
     * @mbggenerated
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.add_time
     *
     * @param addTime the value for queue_mq_config.add_time
     *
     * @mbggenerated
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column queue_mq_config.update_time
     *
     * @return the value of queue_mq_config.update_time
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column queue_mq_config.update_time
     *
     * @param updateTime the value for queue_mq_config.update_time
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}