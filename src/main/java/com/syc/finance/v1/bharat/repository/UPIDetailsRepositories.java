package com.syc.finance.v1.bharat.repository;

import com.syc.finance.v1.bharat.entity.UpiInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UPIDetailsRepositories extends JpaRepository<UpiInformation, String> {
}
