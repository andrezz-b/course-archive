<a id="readme-top"></a>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="frontend/public/archive.svg" alt="Logo" width="80" height="80">
  </a>

<h3 align="center">Course Archive</h3>

  <p align="center">
    A web system for students to share, search, comment, and rate learning and exam materials, featuring user role access control, tagging, and grouping systems.
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#features">Features</a></li>
        <li><a href="#built-with">Built With</a></li>
            <ul>
              <li><a href="#frontend">Frontend</a></li>
              <li><a href="#backend">Backend</a></li>
              <li><a href="#other">Other</a></li>
            </ul>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#preview-mode">Preview mode</a></li>
        <ul>
          <li><a href="#installation">Installation</a></li>
          <li><a href="#usage">Usage</a></li>
      </ul>
      </ul>
    </li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## About The Project

![Product Name Screen Shot][product-screenshot]

Course archive is a web system developed for students to share learning and exam materials.
In contrast to conventional LMS systems, where the content management is focused on the teachers, this
system allows students to publish, search, comment, and rate documents - resources for learning and exam
preparation - themselves. The solution includes access control for different user roles, a tag and group
system that facilitates the search for target materials, and the ability to comment on and rate content,
which helps to highlight the high-quality and most helpful learning resources. The system aims to provide
students with an organized and accessible tool to share such valuable resources easily. Particular
attention is paid to the design of the user interface to implement a solution that is responsive,
intuitive, and easy to use on different screens.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

### Features

- **Student-Centric Content Management**: Students can publish, search, comment, and rate documents.
- **User Roles and Access Control**: Different roles such as User, Manager, and Administrator with specific permissions.
- **Tag and Group System**: Facilitates the search for target materials.
- **Commenting and Rating**: Helps highlight high-quality and most helpful learning resources.
- **Responsive Design**: Intuitive and easy-to-use interface on different screens

### Built With

#### Frontend

[![React][React.js]][React-url]
[![ReactQuery][ReactQuery]][ReactQuery-url]
[![TypeScript][TypeScript]][TypeScript-url]
[![TailwindCSS][tailwindcss]][tailwindcss-url]
[![ShadcnUi][ShadcnUi]][ShadcnUi-url]

#### Backend

[![Spring][Spring]][Spring-url]
[![SpringBoot][SpringBoot]][SpringBoot-url]
[![PostgreSQL][PostgreSQL]][PostgreSQL-url]

#### Other

[![Docker][docker]][docker-url]
<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->

## Getting Started

### Preview mode

Preview mode will run the application with a pre-configured database and some example data using [Docker][docker-url].

#### Installation

1. Clone the repo
   ```sh
   git clone git@github.com:andrezz-b/course-archive.git
    ```
2. Run the docker-compose file with the preview profile
    ```sh
    docker compose --profile preview up
    ```
  - Running the command for the first time will take some time to download the images and build the containers.
  - This will set up the backend and frontend services and the database.

#### Usage

- The frontend will be available at [http://localhost:5173](http://localhost:5173)
- Admin user:
  - Username: `admin`
  - Password: `Test01234`
- Admin user can see all the other users in the user listing located in `Navbar -> Account -> Admin -> Users`
- All the other users have the password `Test01234`
- College `Tehnički fakultet Rijeka` has example programs
- Program `Računarstvo` has example courses
- Course `Baze podataka` has example academic years, academic year `2023/2024` has example materials
- All the example materials are an empty black image
- After you stop the docker-compose changes will not be saved and the data will reset to the initial state

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[product-screenshot]: images/materials-page.png

[Spring]: https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white

[Spring-url]: https://spring.io/

[SpringBoot]: https://img.shields.io/badge/spring_boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white

[SpringBoot-url]: https://spring.io/projects/spring-boot

[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB

[React-url]: https://reactjs.org/

[TypeScript]: https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white

[TypeScript-url]: https://www.typescriptlang.org/

[tailwindcss]: https://img.shields.io/badge/tailwindcss-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white

[tailwindcss-url]: https://tailwindcss.com/

[docker]: https://img.shields.io/badge/docker-%232496ED?style=for-the-badge&logo=docker&logoColor=white

[docker-url]: https://www.docker.com/

[PostgreSQL]: https://img.shields.io/badge/postgresql-4169E1?style=for-the-badge&logo=postgresql&logoColor=white

[PostgreSQL-url]: https://www.postgresql.org/

[ShadcnUi]: https://img.shields.io/badge/shadcn%2Fui-000000?style=for-the-badge&logo=shadcnui&logoColor=white

[ShadcnUi-url]: https://ui.shadcn.com/

[ReactQuery]: https://img.shields.io/badge/react_query-FF4154?style=for-the-badge&logo=reactquery&logoColor=white

[ReactQuery-url]: https://tanstack.com/query/latest/docs/framework/react/overview
