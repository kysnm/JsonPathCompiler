package io.github.medjed.jsonpathcompiler;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static io.github.medjed.jsonpathcompiler.Criteria.where;
import static io.github.medjed.jsonpathcompiler.Filter.filter;
import static org.assertj.core.api.Assertions.assertThat;

public class FilterTest extends BaseTest {


    Object json = Configuration.defaultConfiguration().jsonProvider().parse(
            "{" +
            "  \"int-key\" : 1, " +
            "  \"long-key\" : 3000000000, " +
            "  \"double-key\" : 10.1, " +
            "  \"boolean-key\" : true, " +
            "  \"null-key\" : null, " +
            "  \"string-key\" : \"string\", " +
            "  \"string-key-empty\" : \"\", " +
            "  \"char-key\" : \"c\", " +
            "  \"arr-empty\" : [], " +
            "  \"int-arr\" : [0,1,2,3,4], " +
            "  \"string-arr\" : [\"a\",\"b\",\"c\",\"d\",\"e\"] " +
            "}"
    );

    //----------------------------------------------------------------------------
    //
    // EQ
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_eq_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").eq(1)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-key").eq(666)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void int_eq_string_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").eq("1")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-key").eq("666")).apply(createPredicateContext(json))).isEqualTo(false);


        assertThat(Filter.parse("[?(1 == '1')]").apply(createPredicateContext(json))).isEqualTo(true);
        assertThat(Filter.parse("[?('1' == 1)]").apply(createPredicateContext(json))).isEqualTo(true);

        assertThat(Filter.parse("[?(1 === '1')]").apply(createPredicateContext(json))).isEqualTo(false);
        assertThat(Filter.parse("[?('1' === 1)]").apply(createPredicateContext(json))).isEqualTo(false);

        assertThat(Filter.parse("[?(1 === 1)]").apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void long_eq_evals() {
        Assertions.assertThat(filter(Criteria.where("long-key").eq(3000000000L)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("long-key").eq(666L)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void double_eq_evals() {
        Assertions.assertThat(filter(Criteria.where("double-key").eq(10.1D)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("double-key").eq(10.10D)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("double-key").eq(10.11D)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void string_eq_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").eq("string")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").eq("666")).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void boolean_eq_evals() {
        Assertions.assertThat(filter(Criteria.where("boolean-key").eq(true)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("boolean-key").eq(false)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void null_eq_evals() {
        Assertions.assertThat(filter(Criteria.where("null-key").eq(null)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("null-key").eq("666")).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("string-key").eq(null)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // NE
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_ne_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").ne(1)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("int-key").ne(666)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void long_ne_evals() {
        Assertions.assertThat(filter(Criteria.where("long-key").ne(3000000000L)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("long-key").ne(666L)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void double_ne_evals() {
        Assertions.assertThat(filter(Criteria.where("double-key").ne(10.1D)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("double-key").ne(10.10D)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("double-key").ne(10.11D)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void string_ne_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").ne("string")).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("string-key").ne("666")).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void boolean_ne_evals() {
        Assertions.assertThat(filter(Criteria.where("boolean-key").ne(true)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("boolean-key").ne(false)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void null_ne_evals() {
        Assertions.assertThat(filter(Criteria.where("null-key").ne(null)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("null-key").ne("666")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").ne(null)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // LT
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_lt_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").lt(10)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-key").lt(0)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void long_lt_evals() {
        Assertions.assertThat(filter(Criteria.where("long-key").lt(4000000000L)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("long-key").lt(666L)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void double_lt_evals() {
        Assertions.assertThat(filter(Criteria.where("double-key").lt(100.1D)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("double-key").lt(1.1D)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void string_lt_evals() {
        Assertions.assertThat(filter(Criteria.where("char-key").lt("x")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("char-key").lt("a")).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // LTE
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_lte_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").lte(10)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-key").lte(1)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-key").lte(0)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void long_lte_evals() {
        Assertions.assertThat(filter(Criteria.where("long-key").lte(4000000000L)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("long-key").lte(3000000000L)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("long-key").lte(666L)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void double_lte_evals() {
        Assertions.assertThat(filter(Criteria.where("double-key").lte(100.1D)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("double-key").lte(10.1D)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("double-key").lte(1.1D)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // GT
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_gt_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").gt(10)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("int-key").gt(0)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void long_gt_evals() {
        Assertions.assertThat(filter(Criteria.where("long-key").gt(4000000000L)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("long-key").gt(666L)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void double_gt_evals() {
        Assertions.assertThat(filter(Criteria.where("double-key").gt(100.1D)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("double-key").gt(1.1D)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void string_gt_evals() {
        Assertions.assertThat(filter(Criteria.where("char-key").gt("x")).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("char-key").gt("a")).apply(createPredicateContext(json))).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // GTE
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_gte_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").gte(10)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("int-key").gte(1)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-key").gte(0)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void long_gte_evals() {
        Assertions.assertThat(filter(Criteria.where("long-key").gte(4000000000L)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("long-key").gte(3000000000L)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("long-key").gte(666L)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    @Test
    public void double_gte_evals() {
        Assertions.assertThat(filter(Criteria.where("double-key").gte(100.1D)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("double-key").gte(10.1D)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("double-key").gte(1.1D)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // Regex
    //
    //----------------------------------------------------------------------------
    @Test
    public void string_regex_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").regex(Pattern.compile("^string$"))).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").regex(Pattern.compile("^tring$"))).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("null-key").regex(Pattern.compile("^string$"))).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("int-key").regex(Pattern.compile("^string$"))).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // JSON equality
    //
    //----------------------------------------------------------------------------
    @Test
    public void json_evals() {
        String nest = "{\"a\":true}";
        String arr = "[1,2]";
        String json = "{\"foo\":" + arr + ", \"bar\":" + nest + "}";
        Object tree = Configuration.defaultConfiguration().jsonProvider().parse(json);
        Predicate.PredicateContext context = createPredicateContext(tree);
        Filter farr = Filter.parse("[?(@.foo == " + arr + ")]");
        //Filter fobjF = parse("[?(@.foo == " + nest + ")]");
        //Filter fobjT = parse("[?(@.bar == " + nest + ")]");
        assertThat(farr.apply(context)).isEqualTo(true);
        //assertThat(fobjF.apply(context)).isEqualTo(false);
        //assertThat(fobjT.apply(context)).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // IN
    //
    //----------------------------------------------------------------------------
    @Test
    public void string_in_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").in("a", null, "string")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").in("a", null)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("null-key").in("a", null)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("null-key").in("a", "b")).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("string-arr").in("a")).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // NIN
    //
    //----------------------------------------------------------------------------
    @Test
    public void string_nin_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").nin("a", null, "string")).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("string-key").nin("a", null)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("null-key").nin("a", null)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("null-key").nin("a", "b")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-arr").nin("a")).apply(createPredicateContext(json))).isEqualTo(true);

    }

    //----------------------------------------------------------------------------
    //
    // ALL
    //
    //----------------------------------------------------------------------------
    @Test
    public void int_all_evals() {
        Assertions.assertThat(filter(Criteria.where("int-arr").all(0,1)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-arr").all(0,7)).apply(createPredicateContext(json))).isEqualTo(false);
    }
    @Test
    public void string_all_evals() {
        Assertions.assertThat(filter(Criteria.where("string-arr").all("a","b")).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-arr").all("a","x")).apply(createPredicateContext(json))).isEqualTo(false);
    }
    @Test
    public void not_array_all_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").all("a","b")).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // SIZE
    //
    //----------------------------------------------------------------------------
    @Test
    public void array_size_evals() {
        Assertions.assertThat(filter(Criteria.where("string-arr").size(5)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-arr").size(7)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void string_size_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").size(6)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").size(7)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void other_size_evals() {
        Assertions.assertThat(filter(Criteria.where("int-key").size(6)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    @Test
    public void null_size_evals() {
        Assertions.assertThat(filter(Criteria.where("null-key").size(6)).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // EXISTS
    //
    //----------------------------------------------------------------------------
    @Test
    public void exists_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").exists(true)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").exists(false)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("missing-key").exists(true)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("missing-key").exists(false)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // TYPE
    //
    //----------------------------------------------------------------------------
    @Test
    public void type_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").type(String.class)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").type(Number.class)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("int-key").type(String.class)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("int-key").type(Number.class)).apply(createPredicateContext(json))).isEqualTo(true);

        Assertions.assertThat(filter(Criteria.where("null-key").type(String.class)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("int-arr").type(List.class)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // NOT EMPTY
    //
    //----------------------------------------------------------------------------
    @Test
    public void not_empty_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").notEmpty()).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key-empty").notEmpty()).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("int-arr").notEmpty()).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("arr-empty").notEmpty()).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("null-key").notEmpty()).apply(createPredicateContext(json))).isEqualTo(false);
    }

    //----------------------------------------------------------------------------
    //
    // EMPTY
    //
    //----------------------------------------------------------------------------
    @Test
    public void empty_evals() {
        Assertions.assertThat(filter(Criteria.where("string-key").empty(false)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key").empty(true)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("string-key-empty").empty(true)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("string-key-empty").empty(false)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("int-arr").empty(false)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("int-arr").empty(true)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("arr-empty").empty(true)).apply(createPredicateContext(json))).isEqualTo(true);
        Assertions.assertThat(filter(Criteria.where("arr-empty").empty(false)).apply(createPredicateContext(json))).isEqualTo(false);

        Assertions.assertThat(filter(Criteria.where("null-key").empty(true)).apply(createPredicateContext(json))).isEqualTo(false);
        Assertions.assertThat(filter(Criteria.where("null-key").empty(false)).apply(createPredicateContext(json))).isEqualTo(false);
    }


    //----------------------------------------------------------------------------
    //
    // MATCHES
    //
    //----------------------------------------------------------------------------

    @Test
    public void matches_evals() {
        Predicate p = new Predicate() {
            @Override
            public boolean apply(PredicateContext ctx) {
                Map<String, Object> t = (Map<String, Object>) ctx.item();

                Object o = t.get("int-key");

                Integer i = (Integer) o;

                return i == 1;
            }
        };
        Assertions.assertThat(filter(Criteria.where("string-key").eq("string").and("$").matches(p)).apply(createPredicateContext(json))).isEqualTo(true);
    }

    //----------------------------------------------------------------------------
    //
    // OR
    //
    //----------------------------------------------------------------------------
    @Test
    public void or_and_filters_evaluates() {

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("foo", true);
        model.put("bar", false);

        Filter isFoo = filter(Criteria.where("foo").is(true));
        Filter isBar = filter(Criteria.where("bar").is(true));


        Filter fooOrBar = filter(Criteria.where("foo").is(true)).or(Criteria.where("bar").is(true));
        Filter fooAndBar = filter(Criteria.where("foo").is(true)).and(Criteria.where("bar").is(true));

        assertThat(isFoo.or(isBar).apply(createPredicateContext(model))).isTrue();
        assertThat(isFoo.and(isBar).apply(createPredicateContext(model))).isFalse();
        assertThat(fooOrBar.apply(createPredicateContext(model))).isTrue();
        assertThat(fooAndBar.apply(createPredicateContext(model))).isFalse();

    }


    @Test
    public void testFilterWithOrShortCircuit1() throws Exception {
        Object json = Configuration.defaultConfiguration().jsonProvider().parse( "{\"firstname\":\"Bob\",\"surname\":\"Smith\",\"age\":30}");
        assertThat(Filter.parse("[?((@.firstname == 'Bob' || @.firstname == 'Jane') && @.surname == 'Doe')]").apply(createPredicateContext(json))).isFalse();
    }

    @Test
    public void testFilterWithOrShortCircuit2() throws Exception {
        Object json = Configuration.defaultConfiguration().jsonProvider().parse("{\"firstname\":\"Bob\",\"surname\":\"Smith\",\"age\":30}");
        assertThat(Filter.parse("[?((@.firstname == 'Bob' || @.firstname == 'Jane') && @.surname == 'Smith')]").apply(createPredicateContext(json))).isTrue();
    }

    @Test
    public void criteria_can_be_parsed() {

        Criteria criteria = Criteria.parse("@.foo == 'baar'");
        assertThat(criteria.toString()).isEqualTo("@['foo'] == 'baar'");

        criteria = Criteria.parse("@.foo");
        assertThat(criteria.toString()).isEqualTo("@['foo']");
    }
}
