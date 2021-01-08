# RestaVote
#### Voting system for deciding where to have lunch.

REST API using Hibernate/Spring/SpringMVC without frontend.

<ul>
    <li>2 types of users: admin and regular users</li>
    <li>Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)</li>
    <li>Menu changes each day (admins do the updates)</li>
    <li>Users can vote on which restaurant they want to have lunch at</li>
    <li>Only one vote counted per user</li>
    <li>If user votes again the same day:
        <ul>
            <li>If it is before 11:00 we assume that he changed his mind.</li>
            <li>If it is after 11:00 then it is too late, vote can't be changed</li>
        </ul>
    </li>
</ul> 

Each restaurant provides a new menu each day.

#### cURL samples (application deployed at application context `restavote`).
> For windows use `Git Bash`

##### get All Restaurants
`curl -s http://localhost:8080/restavote/restaurants/`

##### create Restaurants
`curl -s -X POST -d '{"description":"Imperial"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restavote/restaurants --user admin@gmail.com:admin`

##### get Restaurant 103
`curl -s http://localhost:8080/restavote/restaurants/103 --user admin@gmail.com:admin`

##### get Restaurants not found
`curl -s -v http://localhost:8080/restavote/restaurants/10 --user admin@gmail.com:admin`

##### update Restaurants
`curl -s -X PUT -d '{"id"="103", "description":"Pronto"}' -H 'Content-Type: application/json' http://localhost:8080/restavote/restaurants/103 --user admin@gmail.com:admin`

##### delete Restaurants
`curl -s -X DELETE http://localhost:8080/restavote/restaurants/103 --user admin@gmail.com:admin`

##### get Menu of day for Restaurant 103
`curl -s http://localhost:8080/restavote/restaurants/103/menuOfDay --user admin@gmail.com:admin`


##### get All Dishes
`curl -s http://localhost:8080/restavote/restaurants/103/dishes --user user@yandex.ru:password`

##### create Dishes
`curl -s -X POST -d '{"day"="2020-02-01", "description":"Созданный ужин", "price"="300"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restavote/restaurants/103/dishes --user admin@gmail.com:admin`

##### get Dishes
`curl -s http://localhost:8080/restavote/restaurants/103/dishes/107 --user admin@gmail.com:admin`

##### get Dishes not found
`curl -s -v http://localhost:8080/restavote/restaurants/103/dishes/10 --user admin@gmail.com:admin`

##### update Dishes
`curl -s -X PUT -d '{"id"="106", "day"="2020-02-03", "description":"Обновленный завтрак", "price"="200"}' -H 'Content-Type: application/json' http://localhost:8080/restavote/restaurants/103/dishes/106 --user admin@gmail.com:admin`

##### delete Dishes
`curl -s -X DELETE http://localhost:8080/restavote/restaurants/103/dishes/106 --user admin@gmail.com:admin`


##### make Vote
`curl -s -X POST http://localhost:8080/restavote/restaurants/103/vote --user user1@yandex.ru:password`

##### clear Votes
`curl -s -X DELETE http://localhost:8080/restavote/restaurants/103/vote --user admin@gmail.com:admin`


##### get Users
`curl -s http://localhost:8080/restavote/admin/users --user admin@gmail.com:admin`

##### get Users 101
`curl -s http://localhost:8080/restavote/admin/users/101 --user admin@gmail.com:admin`

##### register Users
`curl -s -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restavote/profile/register`

##### get Profile
`curl -s http://localhost:8080/restavote/profile --user test@mail.ru:test-password`