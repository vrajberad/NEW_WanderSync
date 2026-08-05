import "./WhyChooseUs.css";

const features = [
  {
    id: 1,
    icon: "🤖",
    title: "AI Trip Planner",
    description:
      "Create personalized travel itineraries instantly based on your interests, duration and budget.",
  },
  {
    id: 2,
    icon: "🌍",
    title: "Verified Packages",
    description:
      "Choose from trusted travel agencies with verified reviews and ratings.",
  },
  {
    id: 3,
    icon: "💳",
    title: "Secure Payments",
    description:
      "Safe and encrypted payment gateway with instant booking confirmation.",
  },
  {
    id: 4,
    icon: "📞",
    title: "24×7 Support",
    description:
      "Travel confidently with our dedicated customer support anytime.",
  },
];

function WhyChooseUs() {
  return (
    <section id="about" className="why-section">

      <div className="container">

        <div className="section-heading">

          <span>WHY CHOOSE US</span>

          <h2>Travel Smarter with WanderSync</h2>

          <p>
            Everything you need to plan, customize and book your perfect
            vacation in one place.
          </p>

        </div>

        <div className="row g-4">

          {features.map((feature) => (

            <div className="col-lg-3 col-md-6" key={feature.id}>

              <div className="feature-card">

                <div className="feature-icon">
                  {feature.icon}
                </div>

                <h4>{feature.title}</h4>

                <p>{feature.description}</p>

              </div>

            </div>

          ))}

        </div>

      </div>

    </section>
  );
}

export default WhyChooseUs;