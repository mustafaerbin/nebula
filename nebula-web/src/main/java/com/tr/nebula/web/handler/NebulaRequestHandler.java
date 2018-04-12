package com.tr.nebula.web.handler;

import com.tr.nebula.persistence.api.query.search.SearchModel;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Created by Mustafa Erbin on 20.04.2017.
 */
public class NebulaRequestHandler implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().equals(SearchModel.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {

        return parseFilter(nativeWebRequest);
    }

    private SearchModel parseFilter(NativeWebRequest request){
        SearchModel model = new SearchModel();
        if(request.getParameter("_limit") != null)
            model.setLimit(Integer.valueOf(request.getParameter("_limit")));
        if(request.getParameter("_offset") != null)
            model.setOffset(Integer.valueOf(request.getParameter("_offset")));
        if(request.getParameter("_filter") != null)
            model.setFilterExpression(String.valueOf(request.getParameter("_filter")));
        if(request.getParameter("_q") != null)
            model.setQ(request.getParameter("_q"));
        if(request.getParameter("_sort") != null){
            String[] sortArray = request.getParameter("_sort").split(",");
            model.setSort(sortArray);
        }

        model.setResponse(request.getNativeResponse());

        return model;
    }
}
