package top.ezttf.ad.search;

import top.ezttf.ad.search.vo.SearchRequest;
import top.ezttf.ad.search.vo.SearchResponse;

/**
 * 媒体方 广告检索请求
 *
 * 根据请求对象 获取广告创意数据响应
 * @author yuwen
 * @date 2019/2/1
 */
public interface ISearch {

    SearchResponse fetchAds(SearchRequest request);
}
