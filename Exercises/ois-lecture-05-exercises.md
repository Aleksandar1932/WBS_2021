# Available at the [link](https://chrdebru.github.io/courses/ois/ois-lecture-05-solutions.pdf)

## 1.

```
select ?Concept 
where {[] a ?Concept} 
LIMIT 50
```

## 2.

```
select ?x ?y
where {?x ?y "Madonna"@en}
```

## 3.

```
describe dbr:Madonna
```

## 4.

```
select ?p ?o
where
{
{dbr:Madonna ?p ?o}
union
{?p ?o dbr:Madonna}
}
```

## 5.

```
select ?person ?name
where
{
?person dbo:birthPlace dbr:Brussels .
?person dbo:name ?name .
}
```

## 6.

```
ask
where
{
?s dbo:birthPlace dbr:Brussels .
?s dbo:deathPlace dbr:Paris .
}
```

## 7

```
select ?person ?deathPlace
where
{
?person dbo:birthPlace dbr:Ghent .
?person dbo:deathPlace ?deathPlace.

filter (dbr:Ghent != ?deathPlace).
}
limit(20)
```

## 8.

```
select ?person ?name
where
{
?person a dbo:Person .
?person dbo:name ?name .
}
group by ?person
limit(20)
```

## 9.

```
select ?country ?label
where {
?country a  <http://schema.org/Country>.
?country rdfs:label ?label .
FILTER (langMatches(lang(?label), "fr"))
}
```

## 10.

```
CONSTRUCT {
?person1 dbp:knows ?person2.
?person2 dbp:knows ?person1.
}
WHERE {
?person1 dbo:birthPlace dbr:Brussels .
?person2 dbo:birthPlace dbr:Cologne .
} LIMIT 10
```