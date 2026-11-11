package br.com.fieldops.api.domain.repository;

import br.com.fieldops.api.domain.entity.InspecaoSnapshotItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InspecaoSnapshotItemRepository extends JpaRepository<InspecaoSnapshotItem, Long> {

    List<InspecaoSnapshotItem> findByInspecaoIdOrderByOrdemAsc(Long inspecaoId);

    java.util.Optional<InspecaoSnapshotItem> findByIdAndInspecaoId(Long id, Long inspecaoId);
}

