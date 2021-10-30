# Laboratory Exercise 2

## Part 1:

## Part 2: Working with a book's informations

7.

```
select ?author  ?lang
where {
    dbr:The_Little_Prince dbo:author ?author .
    dbr:The_Little_Prince dbp:language ?lang .
}
```

8. 

```
select ?abstract 
where {
    dbr:The_Little_Prince dbo:abstract ?abstract .
    FILTER(LANG(?abstract) = "" || LANGMATCHES(LANG(?abstract), "en")) .
}
```

9.
```
select distinct ?birthDate ?birthPlace ?deathDate ?deathPlace
where {
    dbr:The_Little_Prince dbo:author ?author .
    ?author dbo:birthDate ?birthDate .
    ?author dbo:birthPlace ?birthPlace .
    ?author dbo:deathDate ?deathDate .  
    ?autrho dbo:deathDate ?deathPlace .
}
limit(1)
```

10.
```
select  distinct ?author ?book ?releaseDate
where {
    dbr:The_Little_Prince dbo:author ?author .
    ?book dbo:author ?author .
    ?book dbp:releaseDate ?releaseDate
    FILTER(datatype(?releaseDate) = xsd:integer)
}
order by ?releaseDate
```

11.
```
select ?publisher ?releaseDate
where {
    dbr:The_Little_Prince dbo:publisher ?publisher .
    dbr:The_Little_Prince dbp:releaseDate ?releaseDate .
}
limit (1)
```


## Part 3: Working with arbitrary data

### Muscial Band

```
describe dbr:Arctic_Monkeys
```

```
select distinct ?album
where {
    ?song dbo:artist dbr:Arctic_Monkeys .
    ?song dbo:album ?album .
}
```

```
select distinct ?member ?birthPlace ?birthDate
where {
    dbr:Arctic_Monkeys dbo:bandMember ?member.
    ?member dbo:birthPlace ?birthPlace .
    ?member dbo:birthDate ?birthDate .
}
```

### Person

```
select distinct (?abstract as ?descriptionAndBio)
where {
    dbr:Bill_Gates dbo:abstract ?abstract.
    FILTER(LANG(?abstract) = "" || LANGMATCHES(LANG(?abstract), "en")) .
}
```

```
select distinct ?birthPlace ?deathPlace
where {
    dbr:Bill_Gates dbo:birthPlace ?birthPlace.
    OPTIONAL { dbr:Bill_Gates dbo:deathPlace ?deathPlace}
}
```

```
select ?website
where {
    dbr:Bill_Gates dbp:website ?website.
}
```

### Location

```
select ?abstract ?thumbnail
where {
    dbr:Eiffel_Tower dbo:abstract ?abstract .
    dbr:Eiffel_Tower dbo:thumbnail ?thumbnail .
    FILTER(LANG(?abstract) = "" || LANGMATCHES(LANG(?abstract), "en")) .
}
```

```
select ?georss
where {
    dbr:Eiffel_Tower georss:point ?georss.
}
````

## Part 4: Data for custom applicaiton

```
select distinct ?library ?releaseDate ?license  ?author
where {
    { ?library  dbo:genre dbr:JavaScript_library . }
    union
    { ?library dct:subject dbc:JavaScript_libraries . }

    ?library dbo:releaseDate ?releaseDate .
    ?library dbp:license ?license .
    optional {?library dbo:author ?author .}
} 
order by desc(?releaseDate)
```

```
select distinct  ?license  ?software ?softwareAbstract  ?licenseAbstract
    where {
    { ?library  dbo:genre dbr:JavaScript_library . }
    union
    { ?library dct:subject dbc:JavaScript_libraries . }

    ?library dbp:license ?license .
    ?license dbo:abstract ?licenseAbstract .
    FILTER(LANG(?licenseAbstract) = "" || LANGMATCHES(LANG(?licenseAbstract), "en")) .

    ?software dbp:license ?license .
    ?software dbo:abstract ?softwareAbstract .
    FILTER(LANG(?softwareAbstract ) = "" || LANGMATCHES(LANG(?softwareAbstract ), "en") .
} 

```