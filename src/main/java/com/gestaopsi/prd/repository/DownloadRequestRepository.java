package com.gestaopsi.prd.repository;

import com.gestaopsi.prd.entity.DownloadRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadRequestRepository extends JpaRepository<DownloadRequest, Long> {
    List<DownloadRequest> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<DownloadRequest> findByStatus(String status);
}

