import "./Testimonials.css";

const testimonials = [
  {
    id: 1,
    name: "Priya Sharma",
    city: "Pune",
    rating: "★★★★★",
    review:
      "The AI Planner created a perfect itinerary for my Goa trip. Everything was perfectly organized and booking was effortless.",
  },
  {
    id: 2,
    name: "Rahul Patil",
    city: "Mumbai",
    rating: "★★★★★",
    review:
      "Amazing travel packages with secure booking. The recommendations matched exactly what I wanted for my family vacation.",
  },
  {
    id: 3,
    name: "Sneha Kulkarni",
    city: "Nagpur",
    rating: "★★★★★",
    review:
      "Planning a trip with friends became so easy. WanderSync handled everything from hotels to sightseeing beautifully.",
  },
];

function Testimonials() {
  return (
    <section className="testimonial-section">

      <div className="container">

        <div className="section-heading">

          <span>TESTIMONIALS</span>

          <h2>What Our Travelers Say</h2>

          <p>
            Thousands of travelers trust WanderSync for planning memorable
            vacations across India.
          </p>

        </div>

        <div className="row g-3 justify-content-center">

          {testimonials.map((item) => (

            <div className="col-lg-4 col-md-6" key={item.id}>

              <div className="testimonial-card">

                <div className="quote">❝</div>

                <div className="profile-circle">
                  {item.name.charAt(0)}
                </div>

                <h4>{item.name}</h4>

                <span className="city">
                  📍 {item.city}
                </span>

                <div className="rating">
                  {item.rating}
                </div>

                <p>{item.review}</p>

              </div>

            </div>

          ))}

        </div>

      </div>

    </section>
  );
}

export default Testimonials;