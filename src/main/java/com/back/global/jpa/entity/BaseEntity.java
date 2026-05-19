package com.back.global.jpa.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import static lombok.AccessLevel.PROTECTED;
import static lombok.AccessLevel.PRIVATE;

import java.time.LocalDateTime;
import java.util.Objects;

import static jakarta.persistence.GenerationType.IDENTITY;

@MappedSuperclass // 엔티티의 부모 클래스에는 이걸 달아야 한다.
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private int id;//class레벨에 달린 getter가 작동

    @CreatedDate
   // @Getter(PRIVATE) //외부, 자식클래스에서 접근 불가
    private LocalDateTime createDate;

    @LastModifiedDate
   // @Getter(PROTECTED)//외부에서 호출 불가능, 자식클래스에서 호출 가능
    private LocalDateTime modifyDate;

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity that = (BaseEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}