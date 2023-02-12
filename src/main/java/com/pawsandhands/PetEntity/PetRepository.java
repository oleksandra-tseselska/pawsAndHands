package com.pawsandhands.PetEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Optional;
@Repository
public interface PetRepository extends CrudRepository<Pet, Long> {
    Pet findByTypeOrderByNickname(String type);

@Override
ArrayList<Pet> findAll();

Optional<Pet> findById(Long aLong);

Pet findPetById(Long id);

ArrayList<Pet> findAllByOrderByNickname();


}

