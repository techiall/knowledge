package top.techial.knowledge.config;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import top.techial.knowledge.security.UserPrincipal;

/**
 * @author techial
 */
@Configuration
@EnableSwagger2
@Profile(Constants.DEV)
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .ignoredParameterTypes(UserPrincipal.class)
                .select()
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .directModelSubstitute(Pageable.class, SwaggerPageable.class);
    }

    public static class SwaggerPageable {
        @ApiModelProperty(value = "Number of records per page", example = "20")
        private Integer size;

        @ApiModelProperty(value = "Results page you want to retrieve (0..N)", example = "0")
        private Integer page;

        @ApiModelProperty(value = "Sorting criteria in the format: property(,asc|desc)." +
                "Default sort order is ascending. Multiple sort criteria are supported.")
        private String sort;

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }

        public Integer getPage() {
            return page;
        }

        public void setPage(Integer page) {
            this.page = page;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }
    }
}
