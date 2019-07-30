package com.php25.mediamicroservice.server.repository;

import com.php25.mediamicroservice.server.model.Img;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author: penghuiping
 * @date: 2019/1/4 11:09
 * @description:
 */
public interface ImgRepository extends PagingAndSortingRepository<Img, String> {
}
