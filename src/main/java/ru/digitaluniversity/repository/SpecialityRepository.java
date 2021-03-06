package ru.digitaluniversity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.digitaluniversity.entity.Speciality;

@Repository
public interface SpecialityRepository extends JpaRepository<Speciality, Integer> {
}
