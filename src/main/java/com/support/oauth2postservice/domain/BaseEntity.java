package com.support.oauth2postservice.domain;

import com.support.oauth2postservice.util.constant.ColumnConstants;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public abstract class BaseEntity {

  @CreatedBy
  @Column(name = ColumnConstants.Name.CREATED_BY, updatable = false)
  protected String createdBy;

  @CreatedDate
  @Column(name = ColumnConstants.Name.CREATED_AT, updatable = false)
  protected LocalDateTime createdAt;

  @LastModifiedBy
  @Column(name = ColumnConstants.Name.MODIFIED_BY)
  protected String modifiedBy;

  @LastModifiedDate
  @Column(name = ColumnConstants.Name.MODIFIED_AT)
  protected LocalDateTime modifiedAt;
}
