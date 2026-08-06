import { useNavigate } from "react-router-dom";
import "./Categories.css";

import beachImage from "../../assets/beach.avif";
import mountainImage from "../../assets/mountain.jpg";
import adventureImage from "../../assets/adventure.jpg";
import spiritualImage from "../../assets/spiritual.jpg";
import wildlifeImage from "../../assets/wildlife.jpg";

const categories = [
  {
    id: 1,
    title: "Beach",
    value: "beach",
    image: beachImage,
  },
  {
    id: 2,
    title: "Mountains",
    value: "mountains",
    image: mountainImage,
  },
  {
    id: 3,
    title: "Adventure",
    value: "adventure",
    image: adventureImage,
  },
  {
    id: 4,
    title: "Spiritual",
    value: "spiritual",
    image: spiritualImage,
  },
  {
    id: 5,
    title: "Wildlife",
    value: "wildlife",
    image: wildlifeImage,
  },
];

function Categories() {

  const navigate = useNavigate();

  const handleCategoryClick = (category) => {
    navigate(`/search?category=${category}`);
  };

  return (
    <section id="categories" className="categories-section">

      <div className="container">

        {/* Heading */}

        <div className="categories-heading">

          <span className="categories-tag">
            Explore India
          </span>

          <h2>
            Explore by Category
          </h2>

          <p>
            Discover destinations based on the kind of
            travel experience you love.
          </p>

        </div>


        {/* Category Cards */}

        <div className="row justify-content-center g-4">

          {categories.map((category) => (

            <div
              className="col-lg col-md-4 col-sm-6"
              key={category.id}
            >

              <div
                className="category-card"
                onClick={() =>
                  handleCategoryClick(category.value)
                }
                role="button"
                tabIndex="0"
              >

                <div className="category-image">

                  <img
                    src={category.image}
                    alt={category.title}
                  />

                  <div className="category-overlay"></div>

                  <div className="category-content">

                    <h5>
                      {category.title}
                    </h5>

                    <span>
                      Explore Trips →
                    </span>

                  </div>

                </div>

              </div>

            </div>

          ))}

        </div>

      </div>

    </section>
  );
}

export default Categories;