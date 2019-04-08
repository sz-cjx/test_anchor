package com.arbfintech.microservice.loan.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.*;

/**
 * @author Wade He
 */
@Entity
@Table(name="loan_document")
public class Document {

	@Id
	@Column(length = 10)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(length = 10)
	private Integer loanId;

	@Column(length = 32)
	private String letterTemplate;

	@Column(length = 32)
	private Long documentCreateTime;

	@Column(length = 32)
	private Integer documentStatus;

	@Column(length = 32)
	private Long documentSignatureTime;

	@Column(length = 32)
	private String documentSignerName;

	@Column(length = 32)
	private String documentUrl;

	@Column(length = 32)
	private String documentImgUrl;

	@Column(length = 32)
	private String certificateUrl;

	@Column(length = 32)
	private String certificateImgUrl;

	@Column(length = 32)
	private String previewUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getLoanId() {
		return loanId;
	}

	public void setLoanId(Integer loanId) {
		this.loanId = loanId;
	}

	public String getLetterTemplate() {
		return letterTemplate;
	}

	public void setLetterTemplate(String letterTemplate) {
		this.letterTemplate = letterTemplate;
	}

	public Long getDocumentCreateTime() {
		return documentCreateTime;
	}

	public void setDocumentCreateTime(Long documentCreateTime) {
		this.documentCreateTime = documentCreateTime;
	}

	public Integer getDocumentStatus() {
		return documentStatus;
	}

	public void setDocumentStatus(Integer documentStatus) {
		this.documentStatus = documentStatus;
	}

	public Long getDocumentSignatureTime() {
		return documentSignatureTime;
	}

	public void setDocumentSignatureTime(Long documentSignatureTime) {
		this.documentSignatureTime = documentSignatureTime;
	}

	public String getDocumentSignerName() {
		return documentSignerName;
	}

	public void setDocumentSignerName(String documentSignerName) {
		this.documentSignerName = documentSignerName;
	}

	public String getDocumentUrl() {
		return documentUrl;
	}

	public void setDocumentUrl(String documentUrl) {
		this.documentUrl = documentUrl;
	}

	public String getDocumentImgUrl() {
		return documentImgUrl;
	}

	public void setDocumentImgUrl(String documentImgUrl) {
		this.documentImgUrl = documentImgUrl;
	}

	public String getCertificateUrl() {
		return certificateUrl;
	}

	public void setCertificateUrl(String certificateUrl) {
		this.certificateUrl = certificateUrl;
	}

	public String getCertificateImgUrl() {
		return certificateImgUrl;
	}

	public void setCertificateImgUrl(String certificateImgUrl) {
		this.certificateImgUrl = certificateImgUrl;
	}

	public String getPreviewUrl() {
		return previewUrl;
	}

	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}

	@Override
    public String toString() {
        return JSON.toJSONString(this);
    }

	
}
