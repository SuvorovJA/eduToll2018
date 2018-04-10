package edu.tracker.crud;

/*
 * тестирует: создание inmem базы Н2, работу класса Crud,PointDTOEntity, создание, удаление, модификация, чтение записей
 * на реальном SpringBoot и контексте
 */

import edu.tracker.context.InjectionContext;
import edu.tracker.repository.PointDTOEntity;
import edu.tracker.repository.repo.PointDTORepository;
import edu.tracker.crud.Crud;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

//import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = InjectionContext.class)   //OR @SpringBootTest(classes = InjectionContext.class)
@RestClientTest //FOR Consider defining a bean of type 'o.s.b.w.c.RestTemplateBuilder' in your configuration.
public class CrudIntegrationTest {

    @Autowired
    private PointDTORepository pointDTORepository;

    @Autowired
    private Crud crud;

    private PointDTOEntity[] pe = new PointDTOEntity[10];

    @Before
    public void before() {
        for (int i = 0; i < 10; i++) {
            // String.valueOf(number); Integer.toString(number); "" + number;
            pe[i] = new PointDTOEntity(i + 1, i, i, "" + i, i);
        }
        for (PointDTOEntity pei : pe) {
            pointDTORepository.save(pei);
        }
    }

    @Test
    public void run() {
        crud.read(); // контроль вывода в логи. как?
        //
        crud.delete(pe[0]);
        crud.delete(pe[5]);
        crud.read();
        //
        pe[6].setAutoId("TEST");
        crud.update(pe[6]);
        assertEquals("TEST", crud.read(7).getAutoId());
        crud.read();
        //
        assertEquals(pe[1], crud.read(2));
    }


}