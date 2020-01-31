var app1 = new Vue({
    el: '#app-1',
    data: {
        loading: true,

        // Filter the list of users
        filter: {
            activeOnly: true,
            cityStartsWith: '',
            orderByCreationDate: false,
            orderType: 'asc'
        },

        // List of users
        users: [],

        // Allow creates a new user
        user: { name: null, surname: null, email: null, city: null, birthdate: null, active: true },

        // Message in the create user modal
        errorMessage: null,

        // Message in the main page
        message: null,
        messageType: 'primary'
    },
    methods: {

        /**
         * Search method: allows filter the user list
         */
        search: function () {
            this.message = null
            axios
                .get('/api/users', { params: this.filter })
                .then(response => {
                    this.users = response.data.map(user => this.parseUser(user))
                })
                .catch(error => {
                    console.log(error.response)
                    switch (error.response.status) {
                        case 400:
                        case 429:
                            if (error.response.data.message) {
                                this.setMessage(error.response.data.message, 'danger')
                                break;
                            }
                        default:
                            this.setMessage('An error ocurred!', 'danger')
                    }
                })
                .finally(() => this.loading = false)
        },
        // Parse dates to avoid create vuejs directives
        parseUser: function(user) {
            user.birthday = moment(String(user.birthday)).format('MM/DD/YYYY')
            user.createdAt = moment(String(user.createdAt)).format('MM/DD/YYYY')
            return user
        },

        // Prepares a new empty user data and show the modal to create new one
        newUser: function() {
            this.errorMessage = null
            this.user = { name: null, surname: null, email: null, city: null, birthdate: null, active: true }
            $('#addNewUserModal').modal('show')
        },

        // Calls post method to create the new user
        createUser: function() {
            this.errorMessage = null
            axios.post('/api/users', this.user)
            .then(response => {
                this.users.unshift(this.parseUser(response.data))
                this.setMessage("The user " + response.data.name + " was created with id " + response.data.id + ".")
                $('#addNewUserModal').modal('hide')
            })
            .catch(error => {
                console.log('Create user error', error.response)
                switch (error.response.status) {
                    case 400:
                    case 429:
                        if (error.response.data.message) {
                            this.errorMessage = error.response.data.message
                            break;
                        }
                    default:
                        this.errorMessage = 'An error ocurred!'
                }
            })
        },

        /**
         * Shows a new message in the main page
         * @param {*} message 
         * @param {*} type 
         */
        setMessage: function(message, type) {
            if (!type) type = 'primary'
            this.message = message
            this.messageType = type
        }
    },
    mounted() {
        // First request to get the user list
        this.search()
    }
})

