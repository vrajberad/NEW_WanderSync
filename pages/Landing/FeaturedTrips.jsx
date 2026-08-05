import "./FeaturedTrips.css";

import manaliImage from "../../assets/manali.webp";
import goaImage from "../../assets/goa.png";
import kashmirImage from "../../assets/kashmir.webp";

const trips = [
  {
    id: 1,
    title: "Manali Adventure",
    location: "Himachal Pradesh",
    duration: "5 Days / 4 Nights",
    rating: "4.9",
    price: "₹18,999",
    image: manaliImage,
  },
  {
    id: 2,
    title: "Goa Beach Escape",
    location: "Goa",
    duration: "4 Days / 3 Nights",
    rating: "4.8",
    price: "₹12,999",
    image: goaImage,
  },
  {
    id: 3,
    title: "Kashmir Paradise",
    location: "Jammu & Kashmir",
    duration: "6 Days / 5 Nights",
    rating: "5.0",
    price: "₹25,999",
    image: kashmirImage,
  },
];

function FeaturedTrips() {
  return (
    <section id="featured" className="featured-section">

      <div className="container">

        <div className="section-title">

          <span>POPULAR PACKAGES</span>

          <h2>Featured Trips</h2>

          <p>
            Choose from our handpicked travel experiences and start your next
            unforgettable journey.
          </p>

        </div>

        <div className="row g-3">

          {trips.map((trip) => (

            <div className="col-lg-4" key={trip.id}>

              <div className="trip-card">

                <div className="trip-image">

                  <img src={trip.image} alt={trip.title} />

                  <span className="price-tag">
                    {trip.price}
                  </span>

                </div>

                <div className="trip-content">

                  <small>{trip.location}</small>

                  <h4>{trip.title}</h4>

                  <div className="trip-info">

                    <span>⭐ {trip.rating}</span>

                    <span>{trip.duration}</span>

                  </div>

                  <button className="btn trip-btn">
                    View Details
                  </button>

                </div>

              </div>

            </div>

          ))}

        </div>

      </div>

    </section>
  );
}

export default FeaturedTrips;