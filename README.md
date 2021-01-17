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

#### BUILDING
> mvn clean package -DskipTests=true org.codehaus.cargo:cargo-maven2-plugin:1.8.2:run

#### Swagger API
http://localhost:8080/restavote/swagger-ui.html