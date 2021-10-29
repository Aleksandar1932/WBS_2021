# Laboratory Exercise 2

## Part 1:

## Part 2: Working with a book's informations

7.

```sq
select ?author  ?lang
where {
dbr:The_Little_Prince dbo:author ?author.
dbr:The_Little_Prince dbp:language ?lang.

}
```