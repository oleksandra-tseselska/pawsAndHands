package com.pawsandhands.PetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;
@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {
    Pet findByTypeSexOrderByNickname(String type, String sex);

@Override
ArrayList<Pet> findAll();

@Override
Optional<Pet> findById(Long aLong);
}

