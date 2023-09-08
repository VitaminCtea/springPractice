package spring.web;

import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceView;
import spring.data.Spittle;
import spring.repository.imp.SpittleRepositoryImp;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.hasItems;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class SpittleControllerTest {
    @Test
    public void shouldShowRecentSpittles() throws Exception {
        List<Spittle> expectedSpittles = createSpittleList(20);
        SpittleRepositoryImp mockRepository = mock(SpittleRepositoryImp.class);
        when(mockRepository.findSpittles(238900, 50)).thenReturn(expectedSpittles);
        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc =
                MockMvcBuilders.standaloneSetup(controller)
                        .setSingleView(new InternalResourceView("/WEB-INF/views/spittles.jsp"))
                        .build();
        mockMvc.perform(get("/spittles?max=238900&count=50"))
                .andExpect(view().name("spittles"))
                .andExpect(model().attributeExists("spittleList"))
                .andExpect(model().attribute("spittleList", hasItems(expectedSpittles.toArray())));

        Class<?> cls = SpittleControllerTest.class;
        System.out.println(cls.getTypeName());  // 返回带有包名的路径信息 spring.web.SpittleControllerTest
    }

    private List<Spittle> createSpittleList(int count) {
        return IntStream.range(0, count)
                .mapToObj(x -> new Spittle("Spittle " + x, new Date()))
                .collect(Collectors.toList());
    }

    @Test
    public void testSpittle() throws Exception {
        Spittle expectedSpittle = new Spittle("Hello", new Date());
        SpittleRepositoryImp mockRepository = mock(SpittleRepositoryImp.class);
        when(mockRepository.findOne(12345)).thenReturn(expectedSpittle);
        SpittleController controller = new SpittleController(mockRepository);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(get("/spittles/12345"))
                .andExpect(view().name("spittle"))
                .andExpect(model().attributeExists("spittle"))
                .andExpect(model().attribute("spittle", expectedSpittle));
    }
}
