package cz.upce.fei.nnpia.repository;

import cz.upce.fei.nnpia.domain.Desk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeskRepository extends JpaRepository<Desk, Long> {
}
