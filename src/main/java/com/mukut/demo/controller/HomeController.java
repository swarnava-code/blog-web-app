package com.mukut.demo.controller;

import com.mukut.demo.comparator.*;
import com.mukut.demo.entity.Post;
import com.mukut.demo.entity.Tag;
import com.mukut.demo.model.AuthorModel;
import com.mukut.demo.model.PageModel;
import com.mukut.demo.model.SortModel;
import com.mukut.demo.model.TagModel;
import com.mukut.demo.repo.CommentRepository;
import com.mukut.demo.repo.PostRepository;
import com.mukut.demo.repo.TagRepository;
import com.mukut.demo.repo.UserRepository;
import com.mukut.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private CommentRepository commentRepository;

    final int LIMIT = 5;
    PostService postService = new PostService();
    AuthorModel authorModel = new AuthorModel();
    TagModel tagsModel = new TagModel();

    SortModel sortModel = new SortModel();
    final int START = 1;

    @GetMapping("/")
    public String getBlogList(
            @RequestParam(value = "search", required = false) String keyword,
            @RequestParam(value = "start", required = false) Integer start,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "publishedAt", required = false) String publishedAt,
            @RequestParam(value = "sortField", required = false) String sortField,
            @RequestParam(value = "order", required = false) String order, Model model) {
        List<Post> thePosts = null;
        Set<Post> postByKeywordSearch = null;
        Set<Post> postByAuthor = null;
        Set<Post> mergedSet = new HashSet<>();
        thePosts = new PostService().findAll(postRepository);

        if (publishedAt != null) {
            mergedSet.addAll(postService.findByDate(postRepository, publishedAt));
        }

        if (keyword != null && keyword.replaceAll(" ", "").length() != 0) {
            postByKeywordSearch = postRepository.findByKeyword(keyword);
            mergedSet.addAll(searchTag(keyword));
            model.addAttribute("search", keyword);
        }
        if (author != null && author.length() > 1 && (!author.equals("all"))) {
            List<String> authorList = new HelperService().makeListFromCSV(author);
            authorModel.setSwarnava(false);
            authorModel.setGuddu(false);
            authorModel.setKalaiya(false);
            authorModel.setDhritimoy(false);
            for (String authorName : authorList) {
                postByAuthor = postRepository.findByAuthor(authorName);
                mergedSet.addAll(postByAuthor);
                if (authorName.equals("swarnava")) {
                    authorModel.setSwarnava(true);
                }
                if (authorName.equals("guddu")) {
                    authorModel.setGuddu(true);
                }
                if (authorName.equals("kalaiya")) {
                    authorModel.setKalaiya(true);
                }
                if (authorName.equals("dhritimoy")) {
                    authorModel.setDhritimoy(true);
                }
            }
        } else {
            authorModel.setSwarnava(false);
            authorModel.setGuddu(false);
            authorModel.setKalaiya(false);
            authorModel.setDhritimoy(false);
        }

        if (tag != null && tag.length() > 1 && (!tag.equals("all"))) {
            List<String> tagList = new HelperService().makeListFromCSV(tag);

            tagsModel.setTechnology(false);
            tagsModel.setLifestyle(false);
            tagsModel.setMotivation(false);
            tagsModel.setPolitics(false);
            tagsModel.setCulture(false);

            for (String tagName : tagList) {
                mergedSet.addAll(searchTag(tagName));
                if (tagName.equals("technology")) {
                    tagsModel.setTechnology(true);
                }
                if (tagName.equals("lifestyle")) {
                    tagsModel.setLifestyle(true);
                }
                if (tagName.equals("motivation")) {
                    tagsModel.setMotivation(false);
                }
                if (tagName.equals("politics")) {
                    tagsModel.setPolitics(true);
                }
                if (tagName.equals("culture")) {
                    tagsModel.setCulture(true);
                }
            }
        } else {
            tagsModel.setTechnology(false);
            tagsModel.setLifestyle(false);
            tagsModel.setMotivation(false);
            tagsModel.setPolitics(false);
            tagsModel.setCulture(false);
        }

        if (postByAuthor != null) {
            mergedSet.addAll(postByAuthor);
        }
        if (postByKeywordSearch != null) {
            mergedSet.addAll(postByKeywordSearch);
        }

        List<Post> convertedList = new ArrayList<>();
        convertedList.addAll(mergedSet); //convert set to list

        int size;
        if (start == null) {
            start = 0;
        }
        if (limit == null) {
            limit = LIMIT;
        }

        List<PageModel> paginationUrl;
        List<Post> sortedList;
        if (!convertedList.isEmpty()) {
            sortedList = sort(convertedList, sortField, order);
            size = sortedList.size();
            paginationUrl = pagination(sortedList, start, limit);
            model.addAttribute("posts_list", createSubList(sortedList, start, limit)); //mergedSet
        } else {
            sortedList = sort(thePosts, sortField, order);
            size = sortedList.size();
            paginationUrl = pagination(sortedList, start, limit);
            model.addAttribute("posts_list", createSubList(sortedList, start, limit)); ////thePosts
        }
        model.addAttribute("totalSize", size);
        model.addAttribute("authorsModel", authorModel);
        model.addAttribute("tagsModel", tagsModel);
        model.addAttribute("sortModel", sortModel);
        model.addAttribute("date", date);
        model.addAttribute("pagination_url", paginationUrl);
        return "post/posts_list";
    }
    String date = null;

    public List<Post> createSubList(List<Post> post, int start, int limit) {
        int fromIndex = start, toIndex = limit, size = post.size();
        if (start >= size) {
            fromIndex = size - 1;
        }
        if (start < 0) {
            fromIndex = 0;
        }
        toIndex = limit + start;
        if (toIndex >= size) {
            toIndex = size - 1;
        }
        return post.subList(fromIndex, toIndex);
    }

    public List<Post> sort(List<Post> sortedList, String sortField, String order) {
        if (sortField != null) {
            sortModel.setAuthor(false);
            sortModel.setExcerpt(false);
            sortModel.setPublishedAt(false);
            sortModel.setTitle(false);
            sortModel.setAsc(false);
            sortModel.setDesc(false);
            if (sortField.equals("author")) {
                sortModel.setAuthor(true);
                if (order != null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostAuthorComparatorDesc());
                } else {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostAuthorComparatorAsc());
                }
            } else if (sortField.equals("title")) {
                sortModel.setTitle(true);
                if (order != null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostTitleComparatorDesc());
                } else {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostTitleComparatorAsc());
                }
            } else if (sortField.equals("excerpt")) {
                sortModel.setExcerpt(true);
                if (order != null && order.equals("desc")) {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostExcerptComparatorDesc());
                } else {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostExcerptComparatorAsc());
                }
            } else {
                sortModel.setPublishedAt(true);
                if (order != null && order.equals("asc")) {
                    sortModel.setAsc(true);
                    Collections.sort(sortedList, new PostPublishedAtComparatorAsc());
                } else {
                    sortModel.setDesc(true);
                    Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
                }
            }
        } else {
            sortModel.setAuthor(false);
            sortModel.setExcerpt(false);
            sortModel.setPublishedAt(true);
            sortModel.setTitle(false);
            sortModel.setAsc(false);
            sortModel.setDesc(false);
            if (order != null && order.equals("asc")) {
                sortModel.setAsc(true);
                Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
            } else {
                Collections.sort(sortedList, new PostPublishedAtComparatorDesc());
            }
        }
        return sortedList;
    }

    public List<PageModel> pagination(List<Post> posts, int start, int limit) {
        int noOfRow = posts.size();
        List<PageModel> paginationUrl = new ArrayList<PageModel>();
        List<Integer> list = new ArrayList<>();
        int fromIndex = 0, toIndex = LIMIT;
        if (start >= noOfRow) {
            fromIndex = noOfRow - 1;
        }
        if (start < 0) {
            fromIndex = 0;
        }
        toIndex = limit + start;
        if (toIndex >= noOfRow) {
            toIndex = noOfRow - 1;
        }
        if (noOfRow < LIMIT) {
            PageModel page1 = new PageModel();
            page1.setStart(0);
            page1.setLimit(noOfRow);
            page1.setPageNo(1);
            paginationUrl.add(page1);
        } else {
            int n = (noOfRow / LIMIT);
            int pageNo;
            for (pageNo = 1; pageNo <= n; pageNo++) {
                PageModel page1 = new PageModel();
                page1.setStart((pageNo * LIMIT) - LIMIT);
                page1.setLimit((LIMIT));
                page1.setPageNo(pageNo);
                paginationUrl.add(page1);
            }
            int leftNoOfRow = noOfRow - (n * LIMIT);

            if (leftNoOfRow > 0) {
                PageModel page1 = new PageModel();
                page1.setStart((n * LIMIT) - LIMIT);
                page1.setLimit(leftNoOfRow);
                page1.setPageNo(pageNo);
                paginationUrl.add(page1);
            }
        }
        return paginationUrl;
    }

    public Set<Post> searchTag(String tag) {
        List<Tag> tags = tagRepository.findByTag(tag);
        Set<Post> tagSet = new LinkedHashSet<>();
        for (Tag eachTag : tags) {
            Optional<Post> optionalPost = postRepository.findById(eachTag.getPostId());
            if (!optionalPost.isEmpty()) {
                if (optionalPost.get() != null) tagSet.add(optionalPost.get());
            }
        }
        return tagSet;
    }
}
