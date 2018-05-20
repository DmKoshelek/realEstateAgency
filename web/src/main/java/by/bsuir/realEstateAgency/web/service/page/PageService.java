package by.bsuir.realEstateAgency.web.service.page;

import by.bsuir.realEstateAgency.core.bean.SearchForm;
import by.bsuir.realEstateAgency.core.dao.common.FindAllByUserDao;
import by.bsuir.realEstateAgency.core.model.User;
import by.bsuir.realEstateAgency.core.service.common.FindAllBySearchInterface;
import by.bsuir.realEstateAgency.core.service.common.FindAllByUserService;
import by.bsuir.realEstateAgency.core.service.common.FindAllInterface;
import by.bsuir.realEstateAgency.web.bean.pagedList.PagedListPage;
import by.bsuir.realEstateAgency.web.bean.pagedList.Pagination;

public interface PageService {

    int AMOUNT_PHONES_ON_PAGE = 10;

    int getPagesCount(int itemsCount);

    int normalizePageNumber(int pageNumber, int itemsCount);

    Pagination getPagination(int pageNumber, int itemsCount);

    PagedListPage getPagedList(int pageNumber, FindAllInterface objectService);

    PagedListPage getPagedList(int pageNumber, FindAllBySearchInterface objectService, SearchForm searchForm);

    PagedListPage getPagedList(int pageNumber, FindAllByUserService objectService, User user);
}
