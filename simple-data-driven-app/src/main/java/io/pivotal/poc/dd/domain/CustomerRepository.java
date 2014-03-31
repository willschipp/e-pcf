package io.pivotal.poc.dd.domain;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SDJPA repository
 * @author wschipp
 *
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
