package com.bs.mycareer.repository;

import com.bs.mycareer.dto.CareerDto;
import com.bs.mycareer.entity.Career;
import com.bs.mycareer.repository.CareerContentRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

// 이부분을 상속해서 사용할랬는데 결국 jpa에 의해 그럴필요가 없게됨...
//아직 db가 결정되지 않았으니 일단 이 아이를 레포지토리 구현체로 사용하고 나중에 변경하는 걸로 설계! => 진짜 간단한 리스트 DB

@Repository
public class InMemoryCareerRepository implements CareerContentRepository {

    private final List<Career> careerList = new ArrayList<>();

    @Override
    public Career save(Career career) {
        career.setId((long) (careerList.size() + 1));
        careerList.add(career);
        return career;
    }

    @Override
    public Optional<Career> findById(Long id) {
        return careerList.stream()
                .filter(career -> career.getId().equals(id))
                .findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public <S extends Career> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public List<Career> findAll() {
        return new ArrayList<>(careerList);
    }

    @Override
    public List<Career> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public Career updateCareer(Long id, CareerDto careerDto) {
        Optional<Career> existingCareer = findById(id);
        existingCareer.ifPresent(c -> {
            c.setTitle(careerDto.getTitle());
            c.setContents(careerDto.getContents());
        });
        return existingCareer.orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        careerList.removeIf(career -> career.getId().equals(id));
    }

    @Override
    public void delete(Career entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Career> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends Career> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Career> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Career> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Career getOne(Long aLong) {
        return null;
    }

    @Override
    public Career getById(Long aLong) {
        return null;
    }

    @Override
    public Career getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends Career> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Career> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Career> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Career> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Career> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Career> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Career, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public List<Career> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Career> findAll(Pageable pageable) {
        return null;
    }
}
